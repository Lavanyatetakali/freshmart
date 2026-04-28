// ===== FRESHMART DATA STORE =====
const PRODUCTS = [
  // VEGETABLES
  { id: 1, name: 'Organic Tomatoes', cat: 'vegetables', emoji: '🍅', price: 49, oldPrice: 69, unit: '500g', stock: 120, rating: 4.5, reviews: 238, badge: 'SALE', isNew: false },
  { id: 2, name: 'Fresh Spinach', cat: 'vegetables', emoji: '🥬', price: 39, oldPrice: null, unit: '250g', stock: 85, rating: 4.8, reviews: 192, badge: null, isNew: true },
  { id: 3, name: 'Broccoli Crown', cat: 'vegetables', emoji: '🥦', price: 79, oldPrice: 99, unit: '500g', stock: 60, rating: 4.6, reviews: 145, badge: 'SALE', isNew: false },
  { id: 4, name: 'Baby Carrots', cat: 'vegetables', emoji: '🥕', price: 55, oldPrice: null, unit: '400g', stock: 5, rating: 4.4, reviews: 87, badge: null, isNew: false },
  { id: 5, name: 'Sweet Corn', cat: 'vegetables', emoji: '🌽', price: 35, oldPrice: null, unit: '2 pcs', stock: 200, rating: 4.7, reviews: 312, badge: null, isNew: true },
  { id: 6, name: 'Capsicum Mix', cat: 'vegetables', emoji: '🫑', price: 89, oldPrice: 120, unit: '500g', stock: 40, rating: 4.3, reviews: 76, badge: 'SALE', isNew: false },
  { id: 7, name: 'Eggplant', cat: 'vegetables', emoji: '🍆', price: 45, oldPrice: null, unit: '500g', stock: 70, rating: 4.2, reviews: 54, badge: null, isNew: false },
  { id: 8, name: 'Cucumber', cat: 'vegetables', emoji: '🥒', price: 29, oldPrice: null, unit: '500g', stock: 150, rating: 4.5, reviews: 198, badge: null, isNew: false },

  // FRUITS
  { id: 9, name: 'Royal Bananas', cat: 'fruits', emoji: '🍌', price: 59, oldPrice: null, unit: '1 dozen', stock: 300, rating: 4.9, reviews: 567, badge: null, isNew: false },
  { id: 10, name: 'Kashmiri Apples', cat: 'fruits', emoji: '🍎', price: 149, oldPrice: 189, unit: '1 kg', stock: 80, rating: 4.8, reviews: 423, badge: 'SALE', isNew: false },
  { id: 11, name: 'Sweet Oranges', cat: 'fruits', emoji: '🍊', price: 99, oldPrice: null, unit: '1 kg', stock: 110, rating: 4.7, reviews: 278, badge: null, isNew: true },
  { id: 12, name: 'Alphonso Mangoes', cat: 'fruits', emoji: '🥭', price: 299, oldPrice: 399, unit: '1 kg', stock: 45, rating: 5.0, reviews: 892, badge: 'HOT', isNew: false },
  { id: 13, name: 'Watermelon', cat: 'fruits', emoji: '🍉', price: 199, oldPrice: null, unit: '2–3 kg', stock: 30, rating: 4.6, reviews: 167, badge: null, isNew: false },
  { id: 14, name: 'Strawberries', cat: 'fruits', emoji: '🍓', price: 179, oldPrice: 229, unit: '250g', stock: 25, rating: 4.8, reviews: 334, badge: 'SALE', isNew: false },
  { id: 15, name: 'Grapes', cat: 'fruits', emoji: '🍇', price: 129, oldPrice: null, unit: '500g', stock: 90, rating: 4.5, reviews: 201, badge: null, isNew: true },
  { id: 16, name: 'Pomegranate', cat: 'fruits', emoji: '🍎', price: 119, oldPrice: 149, unit: '500g', stock: 3, rating: 4.7, reviews: 156, badge: 'SALE', isNew: false },

  // DAIRY
  { id: 17, name: 'Full Cream Milk', cat: 'dairy', emoji: '🥛', price: 62, oldPrice: null, unit: '1 Liter', stock: 500, rating: 4.8, reviews: 892, badge: null, isNew: false },
  { id: 18, name: 'Amul Butter', cat: 'dairy', emoji: '🧈', price: 58, oldPrice: null, unit: '100g', stock: 200, rating: 4.9, reviews: 1234, badge: null, isNew: false },
  { id: 19, name: 'Greek Yogurt', cat: 'dairy', emoji: '🍦', price: 89, oldPrice: 109, unit: '400g', stock: 120, rating: 4.7, reviews: 445, badge: 'SALE', isNew: false },
  { id: 20, name: 'Paneer (Cottage)', cat: 'dairy', emoji: '🧀', price: 89, oldPrice: null, unit: '200g', stock: 80, rating: 4.8, reviews: 678, badge: null, isNew: false },
  { id: 21, name: 'Cheddar Cheese', cat: 'dairy', emoji: '🧀', price: 149, oldPrice: 189, unit: '200g', stock: 60, rating: 4.6, reviews: 234, badge: 'SALE', isNew: true },
  { id: 22, name: 'Dahi (Curd)', cat: 'dairy', emoji: '🥣', price: 45, oldPrice: null, unit: '500g', stock: 300, rating: 4.7, reviews: 567, badge: null, isNew: false },

  // SNACKS
  { id: 23, name: 'Baked Oats Cookies', cat: 'snacks', emoji: '🍪', price: 79, oldPrice: 99, unit: '200g', stock: 150, rating: 4.5, reviews: 312, badge: 'SALE', isNew: false },
  { id: 24, name: 'Mixed Nuts Trail', cat: 'snacks', emoji: '🥜', price: 249, oldPrice: null, unit: '300g', stock: 70, rating: 4.8, reviews: 456, badge: null, isNew: false },
  { id: 25, name: 'Whole Wheat Bread', cat: 'snacks', emoji: '🍞', price: 55, oldPrice: null, unit: '400g', stock: 200, rating: 4.6, reviews: 789, badge: null, isNew: true },
  { id: 26, name: 'Potato Chips', cat: 'snacks', emoji: '🥔', price: 35, oldPrice: 45, unit: '100g', stock: 400, rating: 4.4, reviews: 1023, badge: 'SALE', isNew: false },
  { id: 27, name: 'Dark Chocolate', cat: 'snacks', emoji: '🍫', price: 129, oldPrice: null, unit: '100g', stock: 90, rating: 4.9, reviews: 567, badge: null, isNew: false },
  { id: 28, name: 'Popcorn Caramel', cat: 'snacks', emoji: '🍿', price: 89, oldPrice: 109, unit: '200g', stock: 120, rating: 4.5, reviews: 234, badge: 'SALE', isNew: false },

  // PULSES
  { id: 29, name: 'Toor Dal', cat: 'pulses', emoji: '🫘', price: 149, oldPrice: null, unit: '1 kg', stock: 200, rating: 4.7, reviews: 445, badge: null, isNew: false },
  { id: 30, name: 'Moong Dal', cat: 'pulses', emoji: '🫘', price: 129, oldPrice: null, unit: '1 kg', stock: 180, rating: 4.8, reviews: 334, badge: null, isNew: false },
  { id: 31, name: 'Chana Dal', cat: 'pulses', emoji: '🫘', price: 99, oldPrice: 120, unit: '1 kg', stock: 220, rating: 4.6, reviews: 278, badge: 'SALE', isNew: false },
  { id: 32, name: 'Masoor Dal', cat: 'pulses', emoji: '🫘', price: 89, oldPrice: null, unit: '1 kg', stock: 160, rating: 4.5, reviews: 212, badge: null, isNew: true },
  { id: 33, name: 'Rajma (Kidney)', cat: 'pulses', emoji: '🫘', price: 139, oldPrice: 169, unit: '1 kg', stock: 100, rating: 4.8, reviews: 398, badge: 'SALE', isNew: false },
  { id: 34, name: 'Black Chana', cat: 'pulses', emoji: '🫘', price: 119, oldPrice: null, unit: '1 kg', stock: 140, rating: 4.7, reviews: 267, badge: null, isNew: false },

  // BEVERAGES
  { id: 35, name: 'Green Tea', cat: 'beverages', emoji: '🍵', price: 149, oldPrice: 189, unit: '25 bags', stock: 200, rating: 4.8, reviews: 654, badge: 'SALE', isNew: false },
  { id: 36, name: 'Cold Brew Coffee', cat: 'beverages', emoji: '☕', price: 299, oldPrice: null, unit: '250ml', stock: 60, rating: 4.9, reviews: 345, badge: null, isNew: true },
  { id: 37, name: 'Mango Juice', cat: 'beverages', emoji: '🥤', price: 49, oldPrice: 65, unit: '200ml', stock: 350, rating: 4.6, reviews: 892, badge: 'SALE', isNew: false },
  { id: 38, name: 'Coconut Water', cat: 'beverages', emoji: '🥥', price: 45, oldPrice: null, unit: '250ml', stock: 180, rating: 4.7, reviews: 456, badge: null, isNew: false },
];

const COUPONS = {
  'FRESHMART20': { discount: 20, type: 'percent', minOrder: 299 },
  'SAVE50': { discount: 50, type: 'flat', minOrder: 499 },
  'NEWUSER10': { discount: 10, type: 'percent', minOrder: 0 },
  'DAIRY15': { discount: 15, type: 'percent', minOrder: 150, category: 'dairy' },
};

// Product search suggestions
const SEARCH_TERMS = [
  'tomato', 'spinach', 'broccoli', 'carrot', 'apple', 'mango', 'banana', 'orange',
  'milk', 'butter', 'cheese', 'paneer', 'yogurt', 'dal', 'rajma', 'chana',
  'cookies', 'chocolate', 'bread', 'chips', 'nuts', 'green tea', 'coffee', 'juice'
];
