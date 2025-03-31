import "./new.scss";
import DriveFolderUploadOutlinedIcon from "@mui/icons-material/DriveFolderUploadOutlined";
import { useState, useEffect } from "react";
import { productInputs } from "../../formSource";
import { renderMatches, useNavigate, useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { getProductByIdApi, updateProduct } from "../../redux/product/productsApi";
import { useLanguage } from "../../context/LanguageContext";

const EditProduct = ({ inputs, title }) => {
  const param = useParams();
  const [type, setType] = useState(0);
  const [isLoading, setLoad] = useState(true);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const language = useLanguage();
  const [brandId, setBrandId] = useState(1);
  const [categoryId, setCategoryId] = useState(1);
  const [shopId, setShopId] = useState(1);
  const [file, setFile] = useState(null);
  const [arrFile, setArrFile] = useState([]);

  const data = useSelector((state) => state.products.oneProduct.data);
  const $ = document.querySelector.bind(document);

  useEffect(() => {
    if (param.productId) {
      getProductByIdApi(dispatch, param.productId);
    }
    setLoad(false);
  }, [isLoading, param]);

  async function getFileFromUrl(url, urls, defaultType = 'image/png') {
    let name = url.split('/').pop();
    console.log(name);
    const response = await fetch(url);
    const data = await response.blob();
    let file = new File([data], name, {
        type: data.type || defaultType,
    });
    let container = new DataTransfer();
    container.items.add(file);
    $('#file').files = container.files;
    setFile(file);
    if (urls && urls.length > 0) {
        let containers = new DataTransfer();
        for (let i = 0; i < urls.length; i++) {
            let responses = await fetch(urls[i]);
            let datas = await responses.blob();
            let files = new File([data], url.split('/').pop(), {
                type: datas.type || defaultType,
            });
            containers.items.add(files);
        }
        $('#file1').files = containers.files;
        setArrFile(containers.files);
    }
}

  useEffect(()=>{
    if (data) {
      $(`input[id="name"]`).defaultValue=data.title;
      $(`input[id="description"]`).defaultValue=data.info;
      $(`input[id="price"]`).defaultValue=data.originPrice;
      $(`input[id="quantity"]`).defaultValue=data.availableQuantity;
      getFileFromUrl(data.img, data.gallery);
    }
  }, [data]);

  // const imagesListRef = ref(storage, "images/");
  const brands = useSelector((state) => state.brands.allBrands.data);
  const categories = useSelector(
    (state) => state.categories.allCategories.data
  );
  const shops = useSelector((state) => state.shops.allShops.data);

  const handleSubmit = (e) => {
    e.preventDefault();
    const product = {
      name: $(`input[id="name"]`).value,
      description: $(`input[id="description"]`).value,
      price: Number.parseInt($(`input[id="price"]`).value),
      quantity: Number.parseInt($(`input[id="quantity"]`).value),
      status: "PRODUCT_TRADING",
      shopId: Number.parseInt(shopId),
      brandId: Number.parseInt(brandId),
      categoryId: Number.parseInt(categoryId),
      descriptions: {
        additionalProp1: "string",
        additionalProp2: "string",
        additionalProp3: "string",
      },
      location: "",
    };
    let formData = new FormData();
    formData.append(
      "data",
      new Blob([JSON.stringify({ ...product })], {
        type: "application/json",
      })
    );
    if (file?.length > 0) {
      formData.append("image", file[0]);
    }
    if (arrFile?.length > 0) {
      if (arrFile?.length > 0) {
        for (let i = 0; i < arrFile.length; i++) {
          formData.append("images", arrFile[i]);
        }
      }
    }
    updateProduct(data.id, formData, dispatch, navigate);
  };
  return (
    <div className="new">
      <div className="newContainer">
        <div className="top">
          <h1>{title}</h1>
        </div>
        <div className="bottom">
          <div className="left">
            <div className="formInput" key="left">
              <label htmlFor="file">
                <img
                  src={
                    file
                      ? URL.createObjectURL(file)
                      : "https://icon-library.com/images/no-image-icon/no-image-icon-0.jpg"
                  }
                  alt=""
                />
              </label>
              <input
                type="file"
                id="file"
                onChange={(e) => {
                  setFile(e.target.files[0]);
                }}
                style={{ display: "none" }}
              />
            </div>
          </div>
          <div className="right">
            <form onSubmit={handleSubmit}>
              {
                <div className="formContainer">
                  {productInputs.map((input) => (
                    <div className="formInput" key={input.id}>
                      <label>{input.label}</label>
                      <input
                        type={input.type}
                        placeholder={input.placeholder}
                        id={input.title}
                      />
                    </div>
                  ))}
                  <div className="formInput">
                    <label htmlFor="file1">
                      Image: <DriveFolderUploadOutlinedIcon className="icon" />
                    </label>
                    <input
                      type="file"
                      id="file1"
                      onChange={(e) => {
                        setArrFile([...e.target.files]);
                      }}
                      style={{ display: "none" }}
                      multiple
                    />
                  </div>
                  <div className="imgcontent">
                    {arrFile.map((img) => (
                      <img src={URL.createObjectURL(img)} alt="" />
                    ))}
                  </div>
                  <div className="formInput">
                    <label>{language["table.brand"]}</label>
                    <select
                      className="formInput"
                      style={{
                        width: "100%",
                      }}
                      required
                      onChange={() => setBrandId(type.id)}
                    >
                      {brands.map((type) => (
                        <option key={type.id} value={type.id} selected={data && data.brand===type.name}>
                          {type.name}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="formInput">
                    <label>{language["table.category"]}</label>
                    <select
                      className="formInput"
                      style={{
                        width: "100%",
                      }}
                      required
                      onChange={() => setCategoryId(type.id)}
                    >
                      {categories.map((type) => (
                        <option key={type.id} value={type.id} selected={data && data.category===type.title}>
                          {type.title}
                        </option>
                      ))}
                    </select>
                  </div>
                  <div className="formInput">
                    <label>{language["table.shop"]}</label>
                    <select
                      className="formInput"
                      style={{
                        width: "100%",
                      }}
                      required
                      placeholder="Chọn cửa hàng"
                      onChange={(e) => setShopId(e.target.value)}
                    >
                      {shops.map((type, index) => (
                        <option key={index} value={type.id} selected={data && data.shop.id===type.id}>
                          {type.name}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>
              }

              <button>{language["action.send"]}</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};
export default EditProduct;
