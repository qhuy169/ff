import { createSlice } from "@reduxjs/toolkit";

export const products = createSlice({
  name: "products",
  initialState: {
    allProducts: {
      data: [],
    },
    pageProduct: {
      data: [],
    },
    oneProduct: {
      data: "",
    },
    filter: {
      data: [],
    },
    // location: {
    //     data: [],
    // },
    productDetail: {
      data: {},
    },
  },
  reducers: {
    getAllProducts: (state, action) => {
      state.allProducts.data = action.payload ? action.payload : [];
    },
    getPageProduct: (state, action, list) => {
      state.pageProduct.data = action.payload?.content || [];
    },
    updateAllProduct: (state, action) => {
      state.allProducts.data = action.payload;
    },
    getOneProduct: (state, action) => {
      state.oneProduct.data = action.payload;
    },
    handleFilter: (state, action) => {
      state.filter.data = action.payload;
    },
    // getLocationProduct: (state, action) => {
    //     state.location.data = action.payload;
    // },
    getProductDetail: (state, action) => {
      state.productDetail.data = action.payload;
    },
    updateDiscussRating: (state, action) => {
      const rating = state.productDetail.data.rating.find(
        (rating) => rating.id === action.payload.idRating
      );
      if (rating) {
        const res = rating.discuss.push(action.payload);
      }
    },
    addOneProduct: (state, action) => {
      state.pageProduct.data = [
        ...(state.pageProduct.data || []),
        action.payload,
      ];
    },
    updateOneProduct: (state, action) => {
      state.pageProduct.data = Array.from(state.pageProduct.data || []).map(
        (item) => (item.id === action.payload.id ? action.payload : item)
      );
    },
    deleteOneProduct: (state, action) => {
      state.pageProduct.data = Array.from(state.pageProduct.data || []).filter(
        (item) => item.id !== action.payload
      );
    },
  },
});
export const {
  updateDiscussRating,
  getAllProducts,
  getPageProduct,
  getOneProduct,
  handleFilter,
  // getLocationProduct,
  getProductDetail,
  updateAllProduct,
  addOneProduct,
  updateOneProduct,
  deleteOneProduct,
} = products.actions;

export default products.reducer;
