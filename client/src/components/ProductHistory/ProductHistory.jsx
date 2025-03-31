import { useState, useEffect, useRef } from 'react';
import { X } from 'react-bootstrap-icons';
import Section from '../Section';
import ProductCard from '../ProductCard';
// import NextArrow from '../Slick/NextArrow';
// import PrevArrow from '../Slick/PrevArrow';
// import Slider from 'react-slick';
import SlideProduct from '../SlideProduct/SlideProduct';
import { productHistory } from '~/helpers/localStorage';
function ProductHistory({ styleTitle }) {
    const section = useRef();
    const [products, setProducts] = useState([]);
    const handleClick = () => {
        section.current.remove();
        productHistory.clearProductHistory();
    };

    useEffect(() => {
        const data = productHistory.getItems();
       
        setProducts(data);
    }, []);
    return (
        <Section
            title="Sản phẩm bạn đã xem"
            styleTitle={styleTitle}
            styles="bg-white mt-[1%] rounded-md"
            rightOption={
                <span onClick={handleClick} className="cursor-pointer">
                    <span>{styleTitle ? 'XÓA LỊCH SỬ' : 'Xóa lịch sử'}</span>
                    <i>
                        <X />
                    </i>
                </span>
            }
            ref={section}
        >
            {/* <div className="w-full">
                <Slider slidesToShow={5} slidesToScroll={1} nextArrow={<NextArrow />} prevArrow={<PrevArrow />}>
                    {products.map((product) => (
                        <div className="w-full" key={product.title}>
                            <div className="mx-4">
                                <ProductCard key={product.title} {...product} />
                            </div>
                        </div>
                    ))}
                </Slider>
            </div> */}
            <SlideProduct products={products}/>
        </Section>
    );
}

export default ProductHistory;
