// ===== CART & WISHLIST MANAGEMENT =====

// --- Cart ---
function getCart() {
  return JSON.parse(localStorage.getItem('fm_cart') || '[]');
}
function saveCart(cart) {
  localStorage.setItem('fm_cart', JSON.stringify(cart));
  updateCartBadge();
}
function addToCart(productId, qty = 1) {
  const cart = getCart();
  const product = PRODUCTS.find(p => p.id === productId);
  if (!product || product.stock === 0) return;
  const existing = cart.find(i => i.id === productId);
  if (existing) {
    existing.qty = Math.min(existing.qty + qty, product.stock);
  } else {
    cart.push({ id: productId, qty });
  }
  saveCart(cart);
  showToast(`🛒 <strong>${product.name}</strong> added to cart!`);
  updateCartBadge();
}
function removeFromCart(productId) {
  let cart = getCart().filter(i => i.id !== productId);
  saveCart(cart);
}
function updateCartQty(productId, qty) {
  const cart = getCart();
  const item = cart.find(i => i.id === productId);
  const product = PRODUCTS.find(p => p.id === productId);
  if (item) {
    if (qty <= 0) { removeFromCart(productId); return; }
    item.qty = Math.min(qty, product.stock);
    saveCart(cart);
  }
}
function clearCart() {
  localStorage.removeItem('fm_cart');
  updateCartBadge();
}
function getCartTotal() {
  return getCart().reduce((sum, item) => {
    const p = PRODUCTS.find(x => x.id === item.id);
    return sum + (p ? p.price * item.qty : 0);
  }, 0);
}
function getCartCount() {
  return getCart().reduce((sum, i) => sum + i.qty, 0);
}
function updateCartBadge() {
  const el = document.getElementById('cartCount');
  if (el) el.textContent = getCartCount();
  const wl = document.getElementById('wishlistCount');
  if (wl) wl.textContent = getWishlist().length;
}

// --- Wishlist ---
function getWishlist() {
  return JSON.parse(localStorage.getItem('fm_wishlist') || '[]');
}
function saveWishlist(wl) {
  localStorage.setItem('fm_wishlist', JSON.stringify(wl));
  updateCartBadge();
}
function toggleWishlist(productId) {
  const wl = getWishlist();
  const idx = wl.indexOf(productId);
  if (idx >= 0) {
    wl.splice(idx, 1);
    showToast('💔 Removed from wishlist');
  } else {
    wl.push(productId);
    const p = PRODUCTS.find(x => x.id === productId);
    showToast(`❤️ <strong>${p?.name}</strong> added to wishlist!`);
  }
  saveWishlist(wl);
  return idx < 0;
}
function isWishlisted(productId) {
  return getWishlist().includes(productId);
}

// --- Toast ---
function showToast(msg) {
  const el = document.getElementById('toastMsg');
  if (el) {
    el.innerHTML = msg;
    const toast = new bootstrap.Toast(document.getElementById('liveToast'), { delay: 2500 });
    toast.show();
  }
}

// --- Coupon ---
let appliedCoupon = null;
function applyCoupon(code, subtotal) {
  const coupon = COUPONS[code.toUpperCase()];
  if (!coupon) return { success: false, msg: 'Invalid coupon code.' };
  if (subtotal < coupon.minOrder) return { success: false, msg: `Minimum order ₹${coupon.minOrder} required.` };
  appliedCoupon = { ...coupon, code: code.toUpperCase() };
  const discount = coupon.type === 'percent'
    ? Math.round(subtotal * coupon.discount / 100)
    : coupon.discount;
  return { success: true, discount, msg: `✅ Coupon applied! You save ₹${discount}` };
}

// --- User Auth (demo) ---
function getUser() {
  return JSON.parse(localStorage.getItem('fm_user') || 'null');
}
function loginUser(email, name) {
  localStorage.setItem('fm_user', JSON.stringify({ email, name, avatar: name.charAt(0).toUpperCase() }));
}
function logoutUser() {
  localStorage.removeItem('fm_user');
  location.href = '../index.html';
}

// Init on load
document.addEventListener('DOMContentLoaded', () => {
  updateCartBadge();
  // Update account btn if logged in
  const user = getUser();
  const btn = document.getElementById('accountBtn');
  if (user && btn) {
    btn.title = user.name;
    btn.innerHTML = `<span style="font-size:0.75rem;font-weight:900">${user.avatar}</span>`;
    btn.href = 'pages/dashboard.html';
  }
  // scroll to top
  const scrollBtn = document.querySelector('.scroll-top');
  if (scrollBtn) {
    window.addEventListener('scroll', () => {
      scrollBtn.classList.toggle('visible', window.scrollY > 300);
    });
  }
});

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' });
}
