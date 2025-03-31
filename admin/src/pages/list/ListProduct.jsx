import "./list.scss";
import { useState, useEffect } from "react";
import Datatable from "./../../components/datatable/Datatable";
import { useLocation } from "react-router-dom";
import { productColumns } from "./../../datatablesource";
import { useDispatch, useSelector } from "react-redux";
import { getAllProductApi } from "../../redux/product/productsApi";
import { getAllBrandsApi } from "../../redux/brand/brandApi";
import { getAllShopsApi } from "../../redux/shop/shopApi";
import { getAllCategoriesApi } from "../../redux/category/categoriesApi";

const ListProduct = () => {
  const locationUrl = useLocation();
  console.log(locationUrl.pathname);

  const dispatch = useDispatch();
  const data = useSelector((state) => state.products.pageProduct.data);
  useEffect(() => {
    getAllProductApi(dispatch, { limit: 9999 });
    getAllCategoriesApi(dispatch);
    getAllBrandsApi(dispatch);
    getAllShopsApi(dispatch);
  }, []);

  return (
    <div>
      <div>
        <Datatable
          rows={data}
          title=""
          productColumns={productColumns()}
          type="products"
        />
      </div>
    </div>
  );
};

export default ListProduct;
