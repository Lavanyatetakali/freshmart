// ===== HOME PAGE APP =====

let currentFilter = 'all';

// ---- Render Products ----
function renderProducts(filter = 'all') {
  const grid = document.getElementById('productGrid');
  if (!grid) return;
  let data = filter === 'all' ? PRODUCTS.slice(0, 12) : PRODUCTS.filter(p => p.cat === filter).slice(0, 8);
  grid.innerHTML = data.map(p => productCard(p)).join('');
}

function productCard(p) {
  const discount = p.oldPrice ? Math.round((p.oldPrice - p.price) / p.oldPrice * 100) : 0;
  const stockClass = p.stock === 0 ? 'out-stock' : p.stock <= 5 ? 'low-stock' : 'in-stock';
  const stockText = p.stock === 0 ? 'Out of Stock' : p.stock <= 5 ? `Only ${p.stock} left` : 'In Stock';
  const isWL = isWishlisted(p.id);
  return `
  <div class="col-6 col-md-4 col-lg-3" data-cat="${p.cat}">
    <div class="product-card">
      <div class="product-img-wrap">
        ${p.badge ? `<span class="product-badge ${p.badge === 'NEW' ? 'new' : ''}">${p.badge}</span>` : ''}
        ${p.isNew && !p.badge ? `<span class="product-badge new">NEW</span>` : ''}
        <span class="product-emoji">${p.emoji}</span>
        <button class="wishlist-btn ${isWL ? 'active' : ''}" onclick="handleWishlist(${p.id}, this)" title="Wishlist">
          <i class="fa${isWL ? 's' : 'r'} fa-heart"></i>
        </button>
      </div>
      <div class="product-body">
        <div class="product-cat">${p.cat}</div>
        <div class="product-name">${p.name}</div>
        <div class="product-unit">${p.unit}</div>
        <div class="product-rating">
          ${'★'.repeat(Math.floor(p.rating))}${'☆'.repeat(5 - Math.floor(p.rating))}
          <span>(${p.reviews})</span>
        </div>
        <div class="price-row">
          <span class="price">₹${p.price}</span>
          ${p.oldPrice ? `<span class="price-old">₹${p.oldPrice}</span>` : ''}
          ${discount ? `<span class="price-discount">${discount}% OFF</span>` : ''}
        </div>
        <span class="stock-badge ${stockClass}">${stockText}</span>
        <div class="qty-add-row">
          <div class="qty-selector">
            <button class="qty-btn" onclick="changeQty(this, -1)">−</button>
            <input class="qty-input" type="number" value="1" min="1" max="${p.stock}" readonly/>
            <button class="qty-btn" onclick="changeQty(this, 1)">+</button>
          </div>
          <button class="btn-add-cart" onclick="handleAddCart(${p.id}, this)" ${p.stock === 0 ? 'disabled' : ''}>
            <i class="fas fa-cart-plus"></i> Add
          </button>
        </div>
      </div>
    </div>
  </div>`;
}

function changeQty(btn, delta) {
  const input = btn.parentElement.querySelector('.qty-input');
  const val = parseInt(input.value) + delta;
  if (val >= 1 && val <= parseInt(input.max)) input.value = val;
}

function handleAddCart(id, btn) {
  const input = btn.closest('.qty-add-row').querySelector('.qty-input');
  const qty = parseInt(input?.value || 1);
  addToCart(id, qty);
  btn.innerHTML = '<i class="fas fa-check"></i> Added!';
  btn.style.background = '#43a047';
  setTimeout(() => {
    btn.innerHTML = '<i class="fas fa-cart-plus"></i> Add';
    btn.style.background = '';
  }, 1500);
}

function handleWishlist(id, btn) {
  const added = toggleWishlist(id);
  btn.classList.toggle('active', added);
  btn.innerHTML = `<i class="fa${added ? 's' : 'r'} fa-heart"></i>`;
}

function filterProducts(cat, btn) {
  currentFilter = cat;
  document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
  btn.classList.add('active');
  renderProducts(cat);
}

// ---- Search ----
function performSearch() {
  const query = document.getElementById('searchInput')?.value.trim();
  if (query) {
    window.location.href = `pages/products.html?q=${encodeURIComponent(query)}`;
  }
}

document.addEventListener('DOMContentLoaded', () => {
  renderProducts();

  // Search suggestions
  const input = document.getElementById('searchInput');
  const suggestions = document.getElementById('searchSuggestions');
  if (input && suggestions) {
    input.addEventListener('input', () => {
      const q = input.value.toLowerCase().trim();
      if (!q) { suggestions.classList.remove('active'); return; }
      const matches = PRODUCTS.filter(p => p.name.toLowerCase().includes(q)).slice(0, 6);
      if (!matches.length) { suggestions.classList.remove('active'); return; }
      suggestions.innerHTML = matches.map(p =>
        `<div class="suggestion-item" onclick="selectSuggestion('${p.name}')">
          <span>${p.emoji}</span>
          <span>${p.name}</span>
          <span style="color:var(--green);font-weight:700;margin-left:auto">₹${p.price}</span>
        </div>`
      ).join('');
      suggestions.classList.add('active');
    });
    input.addEventListener('keydown', e => { if (e.key === 'Enter') performSearch(); });
    document.addEventListener('click', e => {
      if (!e.target.closest('.search-bar')) suggestions.classList.remove('active');
    });
  }
});

function selectSuggestion(name) {
  const input = document.getElementById('searchInput');
  if (input) input.value = name;
  document.getElementById('searchSuggestions')?.classList.remove('active');
  window.location.href = `pages/products.html?q=${encodeURIComponent(name)}`;
}

// ---- Coupon Copy ----
function copyCoupon() {
  navigator.clipboard.writeText('FRESHMART20').then(() => {
    showToast('📋 Coupon <strong>FRESHMART20</strong> copied!');
  });
}

// ---- Newsletter ----
function subscribeNewsletter() {
  const input = document.querySelector('.newsletter-form input');
  if (!input?.value) return alert('Please enter your email');
  if (!input.value.includes('@')) return alert('Please enter a valid email');
  showToast('🎉 Subscribed successfully! Check your inbox.');
  input.value = '';
}
