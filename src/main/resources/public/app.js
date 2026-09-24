// The web housing of the machine: shows the state, sends the customer's actions.
// Every action returns the new state, so the page re-renders everything from it.
'use strict';

// The state comes as plain numbers and names: amounts in cents, drinks by name. Formatting happens here.
const SPOKEN_ALOUD = new Set(['Kein Bier vor 4', 'zu wenig Geld', 'Prost!', 'Mama, ich habe mich angeschissen']);

const slotsEl = document.getElementById('slots');
const displayEl = document.getElementById('display');
const creditEl = document.getElementById('credit');
const messageEl = document.getElementById('message');
const outputTrayEl = document.getElementById('output-tray-content');
const coinReturnEl = document.getElementById('coin-return-content');

let cansInTrayBefore = 0;

// ---- Talking to the machine -------------------------------------------

async function fetchState() {
  const response = await fetch('/api/state');
  if (!response.ok) {
    throw new Error(`GET /api/state antwortet mit ${response.status}`);
  }
  return response.json();
}

async function act(path) {
  try {
    const response = await fetch(path, { method: 'POST' });
    if (!response.ok) {
      throw new Error(`POST ${path} antwortet mit ${response.status}`);
    }
    const state = await response.json();
    render(state);
    if (state.refused) {
      flashDisplay();
    }
    if (SPOKEN_ALOUD.has(state.message)) {
      speak(state.message);
    }
  } catch (error) {
    showOffline(error);
  }
}

// ---- Rendering --------------------------------------------------------

function render(state) {
  displayEl.classList.remove('display--offline');
  creditEl.textContent = `Guthaben ${euro(state.credit)}`;
  messageEl.textContent = state.message;
  renderSlots(state.slots);
  renderOutputTray(state.outputTray);
  renderCoinReturn(state.coinReturn);
}

// The page has no idea how many slots the machine has: it shows whatever the state lists.
function renderSlots(slots) {
  slotsEl.replaceChildren(...slots.map(slot => {
    const button = document.createElement('button');
    button.type = 'button';
    button.className = slot.stock === 0 ? 'slot slot--sold-out' : 'slot';
    button.dataset.drink = slot.drink;
    button.dataset.position = slot.position;
    const priceText = slot.price == null ? '' : `, ${euro(slot.price)}`;
    button.setAttribute('aria-label', `Fach ${slot.position}: ${slot.name} wählen${priceText}, ${slot.stock} Dosen`);

    const number = document.createElement('span');
    number.className = 'slot-number';
    number.textContent = slot.position;

    const name = document.createElement('span');
    name.className = 'slot-name';
    name.textContent = slot.name;

    const cans = document.createElement('span');
    cans.className = 'slot-cans';
    for (let i = 0; i < slot.stock; i++) {
      cans.append(canImage(slot.drink));
    }

    button.append(number, name, cans);
    // The price appears only once the machine knows one.
    if (slot.price != null) {
      const price = document.createElement('span');
      price.className = 'slot-price';
      price.textContent = euro(slot.price);
      button.append(price);
    }
    return button;
  }));
}

function renderOutputTray(cans) {
  outputTrayEl.replaceChildren(...cans.map((can, index) => {
    const image = canImage(can.drink);
    image.setAttribute('aria-label', can.name);
    if (index >= cansInTrayBefore) {
      image.classList.add('can--dropped');
    }
    return image;
  }));
  if (cans.length > 0) {
    outputTrayEl.append(hint('Zum Entnehmen klicken'));
  }
  cansInTrayBefore = cans.length;
}

function renderCoinReturn(coins) {
  coinReturnEl.replaceChildren(...coins.map(cents => {
    const chip = document.createElement('span');
    chip.className = `chip coin--${cents}`;
    chip.textContent = coinLabel(cents);
    return chip;
  }));
  if (coins.length > 0) {
    coinReturnEl.append(hint('Zum Entnehmen klicken'));
  }
}

/** An <svg><use> pointing at the can design of this drink; unknown drinks get the plain can. */
function canImage(drink) {
  const symbolId = document.getElementById(`can-${drink}`) ? `can-${drink}` : 'can-DEFAULT';
  const svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
  svg.setAttribute('class', 'can');
  svg.setAttribute('viewBox', '0 0 40 64');
  svg.setAttribute('role', 'img');
  const use = document.createElementNS('http://www.w3.org/2000/svg', 'use');
  use.setAttribute('href', `#${symbolId}`);
  svg.append(use);
  return svg;
}

/** 150 -> "1,50 €" */
function euro(cents) {
  return `${Math.floor(cents / 100)},${String(cents % 100).padStart(2, '0')} €`;
}

/** What is printed on a coin: 50 -> "50 ct", 200 -> "2 €" */
function coinLabel(cents) {
  return cents % 100 === 0 ? `${cents / 100} €` : `${cents} ct`;
}

function hint(text) {
  const span = document.createElement('span');
  span.className = 'hint';
  span.textContent = text;
  return span;
}

function flashDisplay() {
  displayEl.classList.remove('display--refused');
  // Reflow so the animation restarts when the same message appears twice in a row.
  void displayEl.offsetWidth;
  displayEl.classList.add('display--refused');
}

function showOffline(error) {
  console.error(error);
  displayEl.classList.add('display--offline');
  messageEl.textContent = 'Keine Verbindung zum Automaten';
}

// ---- Speech -----------------------------------------------------------

function speak(text) {
  if (!('speechSynthesis' in window)) {
    return;
  }
  const utterance = new SpeechSynthesisUtterance(text);
  utterance.lang = 'de-DE';
  window.speechSynthesis.cancel();
  window.speechSynthesis.speak(utterance);
}

// ---- Wiring the buttons -----------------------------------------------

slotsEl.addEventListener('click', event => {
  const slot = event.target.closest('[data-drink]');
  if (slot) {
    act(`/api/select/${slot.dataset.drink}`);
  }
});

for (const coinButton of document.querySelectorAll('[data-coin]')) {
  coinButton.addEventListener('click', () => act(`/api/insert/${coinButton.dataset.coin}`));
}

document.getElementById('cancel').addEventListener('click', () => act('/api/cancel'));
document.getElementById('output-tray').addEventListener('click', () => act('/api/take-drinks'));
document.getElementById('coin-return').addEventListener('click', () => act('/api/take-coins'));

fetchState().then(render).catch(showOffline);
