import { ProductService } from "../../services";

import {
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
} from "./productsSlice";

export const HandleFilter = async (dispatch, data) => {
  dispatch(handleFilter(data));
};

export const updateAllProducts = async (dispatch, data) => {
  dispatch(updateAllProduct(data));
};

export const getAllProductByCategory = async (dispatch, category) => {
  let res = await ProductService.getProductByCategory(category);
  dispatch(getAllProducts(res.data));
};

export const getAllProductApi = async (dispatch, params) => {
  let res = await ProductService.getProducts(params);
  dispatch(getPageProduct(res.data));
};

export const getProductDetailApi = async (dispatch, slug) => {
  let res = await ProductService.getProductBySlug(slug);
  dispatch(getProductDetail(res.data));
};

export const getProductByIdApi = async (dispatch, id) => {
  let res = await ProductService.getProductById(id);
  dispatch(getOneProduct(res.data));
};

////

export const createProduct = async (product, dispatch, navigate) => {
  try {
    const res = await ProductService.createProduct(product);
    dispatch(addOneProduct(res.data));
    navigate("/products");
  } catch (err) {
    console.error(err?.message);
  }
};

export const updateProduct = async (
  id,
  formData,
  dispatch,
  navigate
) => {
  try {
    const res = await ProductService.updateProductById(id, formData);
    console.log(formData);
    
    dispatch(updateOneProduct(res.data));
    navigate("/products");
  } catch (err) {
    console.error(err?.message);
  }
};

export const deleteProduct = async (id, dispatch, navigate) => {
  try {
    const res = await ProductService.deleteProductById(id);
    dispatch(deleteOneProduct(id));
    navigate("/products");
  } catch (err) {
    console.error(err?.message);
  }
};
