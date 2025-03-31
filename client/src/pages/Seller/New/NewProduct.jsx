import './new.scss';
import DriveFolderUploadOutlinedIcon from '@mui/icons-material/DriveFolderUploadOutlined';
import { useEffect, useState, useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';
import FileBase64 from 'react-file-base64';
import { CardMedia } from '@material-ui/core';
import { getAllBrandApi } from '../../../redux/brand/brandApi';
import { getAllCategoriesApi } from '../../../redux/category/categoriesApi';
import { getUserByAccess } from './../../../redux/user/userApi';
import { createProduct, getProductByIdApi, updateProduct } from '../../../redux/product/productsApi';
import { getAllProductApi } from '../../../redux/product/productsApi';
const New = ({ title, action, isUpdate }) => {
    const { productId } = useParams();
    console.log('isUpdate', isUpdate);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    // const user = useSelector((state) => state.auth.login?.currentUser);
    const product = useSelector((state) => state.products.oneProduct.data);
    let getuser = JSON.parse(localStorage.getItem('customerInfo'));
    let getlocationed = JSON.parse(localStorage.getItem('customerInfo')) || '';

    useEffect(() => {
        getAllCategoriesApi(dispatch);
        getAllBrandApi(dispatch);
        getAllProductApi(dispatch, { shopId: getuser.shopId });
    }, []);
    const categoriesList = useSelector((state) => state.categories?.allCategory?.data);
    const brandList = useSelector((state) => state.brands?.allBrand?.data);
    const productList = useSelector((state) => state.products?.pageProducts?.data) || [];
    const [file, setFile] = useState();
    const [files, setFiles] = useState();
    const [name, setName] = useState();
    const [price, setPrice] = useState('0');
    const [category_id, setCategory] = useState('1');
    const [brand_id, setBrand] = useState('1');
    const [amount, setAmount] = useState('0');
    const [description, setDescription] = useState();
    const [status, setStatus] = useState('');
    const [productState, setProductState] = useState();
    const [location, setLocation] = useState(getlocationed[0] || '');

    useEffect(() => {
        if (isUpdate) {
            getProductByIdApi(dispatch, productId);
        }
    }, [productId]);

    async function getFileFromUrl(url, urls, defaultType = 'image/png') {
        let name = url.split('/').pop();
        const response = await fetch(url);
        const data = await response.blob();
        let file = new File([data], name, {
            type: data.type || defaultType,
        });
        let container = new DataTransfer();
        container.items.add(file);
        document.getElementById('file').files = container.files;
        setFile(container.files);
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
            document.getElementById('files').files = containers.files;
            setFiles(containers.files);
        }
    }

    useEffect(() => {
        if (isUpdate && product && product.id === Number.parseInt(productId)) {
            getFileFromUrl(product.img, product.gallery);
            setName(product.title);
            setPrice(product.price);
            setCategory(categoriesList.find((item) => item.title === product.category)?.id);
            setBrand(brandList.find((item) => item.name === product.brand)?.id);
            setAmount(product.availableQuantity);
            setDescription(product.description);
            setStatus(product.status);
        }
    }, [isUpdate, product]);

    const handleInsert = (e) => {
        e.preventDefault();

        const newProduct = {
            name: name,
            description: description,
            price: Number(price),
            quantity: Number(amount),
            shopId: Number(getuser.shopId),
            status: 'PRODUCT_TRADING',
            brandId: Number(brand_id),
            categoryId: Number(category_id),
            descriptions: {
                additionalProp1: 'string',
                additionalProp2: 'string',
                additionalProp3: 'string',
            },
            location: 10,
        };
        console.log(newProduct);
        let formData = new FormData();
        formData.append('data', new Blob([JSON.stringify({ ...newProduct })], { type: 'application/json' }));
        if (file?.length > 0) {
            formData.append('image', file[0]);
        }
        if (files?.length > 0) {
            if (files?.length > 0) {
                for (let i = 0; i < files.length; i++) {
                    formData.append('images', files[i]);
                }
            }
        }
        isUpdate
            ? updateProduct(product.id, formData, dispatch, navigate, product)
            : createProduct(formData, dispatch, navigate, productList);
    };
    return (
        <div className="new">
            <div className="newContainer">
                <div className="top">
                    <h1>{title}</h1>
                </div>
                <div className="bottom">
                    <div className="left">
                        <img src={file ? URL.createObjectURL(file[0]) : ``} alt="" />
                        {/* <CardMedia
              image={file || ''}
              title='Title'
              
            /> */}
                    </div>
                    <div className="right">
                        <form onSubmit={handleInsert}>
                            <div className="formInput">
                                <label htmlFor="file">
                                    Image: <DriveFolderUploadOutlinedIcon className="icon" />
                                </label>

                                <input
                                    type="file"
                                    id="file"

                                    onChange={(e) => setFile(e.target.files)}
                                />
                            </div>

                            <div className="formInput">
                                <label htmlFor="files">
                                    Image: <DriveFolderUploadOutlinedIcon className="icon" />
                                </label>

                                <input
                                    type="file"
                                    id="files"
                                    multiple={true}
                                    // files={isUpdate && product && getFileFromUrl(product.img)}
                                    onChange={(e) => setFiles(e.target.files)}
                                />
                            </div>

                            <div className="formInput">
                                <label>Name</label>
                                <input value={name} type="text" onChange={(e) => setName(e.target.value)} />
                            </div>
                            <div className="formInput">
                                <label>Price</label>
                                <input value={price} type="text" onChange={(e) => setPrice(e.target.value)} />
                            </div>

                            <div className="formInput">
                                <label>Amount</label>
                                <input value={amount} type="text" onChange={(e) => setAmount(e.target.value)} />
                            </div>

                            <div className="formInput">
                                <label>Description</label>
                                <input
                                    value={description}
                                    type="text"
                                    onChange={(e) => setDescription(e.target.value)}
                                />
                            </div>



                            <select
                                className="table-group-action-input form-control"
                                onChange={(e) => {
                                    setCategory(e.target.value);
                                }}
                            >
                                {categoriesList?.map((item, index) => (
                                    <option
                                        key={index}
                                        value={item.id}
                                        selected={isUpdate && product && item.title === product.category}
                                        onSelect={() => setCategory(item.id)}
                                    >
                                        {item.title}
                                    </option>
                                ))}
                            </select>

                            <select
                                className="table-group-action-input form-control"
                                onChange={(e) => setBrand(e.target.value)}
                            >
                                {brandList?.map((item, index) => (
                                    <option
                                        key={index}
                                        value={item.id}
                                        selected={isUpdate && product && item.name === product.brand}
                                        onSelect={() => setBrand(item.id)}
                                    >
                                        {item.name}
                                    </option>
                                ))}
                            </select>

                            <button type="submit">Send</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default New;
