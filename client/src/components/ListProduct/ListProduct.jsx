import './listproduct.scss';
import ProductCard from '../ProductCard';
import Slick from '../Slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import Slider from 'react-slick';
import SlideProduct from '../SlideProduct';
import { useSelector } from 'react-redux';

const ListProduct = (props) => {
    const isSlide = props.isSlide;
    let products = props?.products !== null ? props.products : [];
    if (!products) {
        products = [];
    }
 
    return (
        <>
            {isSlide ? (
                <div className="slideproduct">
                    <SlideProduct products={products}></SlideProduct>
                </div>
            ) : (
                <div className="listproduct">
                    {products && products.map((product) => (
                        <ProductCard key={product.id} {...product} />
                    ))}
                </div>
            )}

            <a className="listcontent__btn">Xem tất cả sản phẩm</a>
        </>
    );
};
export default ListProduct;
