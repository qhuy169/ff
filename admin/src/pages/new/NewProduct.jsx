import "./new.scss";
import DriveFolderUploadOutlinedIcon from "@mui/icons-material/DriveFolderUploadOutlined";
import { useState, useEffect } from "react";
import { productInputs } from "../../formSource";
import { useNavigate, useParams } from "react-router-dom";
// import {
//   ref,
//   uploadBytes,
//   getDownloadURL,
//   listAll,
//   list,
// } from "firebase/storage";
// import { storage } from "../../utils/firebase";
// import { v4 } from "uuid";
import { useDispatch, useSelector } from "react-redux";
import { useLanguage } from "../../context/LanguageContext";
import { createProduct } from "../../redux/product/productsApi";

const NewProduct = ({ title }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const language = useLanguage();
  const [brandId, setBrandId] = useState(1);
  const [categoryId, setCategoryId] = useState(1);
  const [shopId, setShopId] = useState(1);
  const [file, setFile] = useState(null);
  const [arrFile, setArrFile] = useState([]);
  // const imagesListRef = ref(storage, "images/");
  const brands = useSelector((state) => state.brands.allBrands.data);
  const categories = useSelector(
    (state) => state.categories.allCategories.data
  );
  const shops = useSelector((state) => state.shops.allShops.data);

  // const uploadFile = () => {
  //     if (imageUpload == null) return;
  //     const imageRef = ref(storage, `images/${imageUpload.name + v4()}`);
  //     uploadBytes(imageRef, imageUpload).then((snapshot) => {
  //         getDownloadURL(snapshot.ref).then((url) => {
  //             setImageUrls((prev) => [...prev, url]);
  //         });
  //     });
  // };
  // useEffect(() => {
  //   listAll(imagesListRef).then((response) => {
  //     response.items.forEach((item) => {
  //       getDownloadURL(item).then((url) => {
  //         // let curentArr = imageUrls.filter(item => item === url)
  //         setImageUrls(url);
  //       });
  //     });
  //   });
  // }, []);
  const handleChangeSlug = (titleP, value) => {
    if (titleP === "title") {
      document.getElementById("slug").value = parseStringToSlug(value);
    }
  };
  const parseStringToSlug = (string) => {
    return string.toLowerCase().split(" ").join("-");
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const $ = document.querySelector.bind(document);
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
    createProduct(formData, dispatch, navigate);
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
                        defaultValue={input.defaultValue}
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
                        <option key={type.id} value={type.id}>
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
                        <option key={type.id} value={type.id}>
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
                        <option key={index} value={type.id}>
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

export default NewProduct;
