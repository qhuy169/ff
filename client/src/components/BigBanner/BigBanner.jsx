import './bigbanner.scss';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import Slider from 'react-slick';
import { Link } from 'react-router-dom';
import './slick.scss'
const images = [
    'https://cdn.vietnammoi.vn/2019/8/23/freeservice-bangchidan-15665444838181728642931.png',
    'https://cdn.vietnammoi.vn/2019/8/23/freeservice-bangchidan-15665444838181728642931.png',
    'https://png.pngtree.com/thumb_back/fh260/back_our/20200701/ourmid/pngtree-atmospheric-high-end-skin-care-product-advertising-background-image_344527.jpg',
    'https://png.pngtree.com/thumb_back/fh260/back_our/20200701/ourmid/pngtree-atmospheric-high-end-skin-care-product-advertising-background-image_344527.jpg',
    'https://png.pngtree.com/thumb_back/fh260/back_our/20200701/ourmid/pngtree-atmospheric-high-end-skin-care-product-advertising-background-image_344527.jpg',
];
const BigBanner = () => {
    return (
        <div className="container__bigbanner">
            <div className="containner__body">
                <div className="containner__first-item rounded-lg text-center">
                    <Slider dots={true} slidesToShow={1} slidesToScroll={1} autoplay={true} autoplaySpeed={2000} className="w-[800px] h-[290px]">
                        {images.map((src, index) => (
                            <div key={index} className="owl-item" style={{ width: '800px', height:'100px'}}>
                                <div className="item">
                                    <Link to="#">
                                        <img src={src} alt="" className='h-full w-full'/>
                                    </Link>
                                </div>
                            </div>
                        ))}
                    </Slider>
                </div>
                <div className="containner__second-item">
                    {/* <div>
                        <a href="" className = "h-[200px] w-[200px]">
                            <img className='w-full h-full' src="https://png.pngtree.com/thumb_back/fh260/back_our/20200701/ourmid/pngtree-atmospheric-high-end-skin-care-product-advertising-background-image_344527.jpg" alt="" />
                        </a>
                    </div>
                    <div>
                        <a href="" className = "h-[200px] w-[200px]">
                            <img className='w-full h-full' src="https://png.pngtree.com/thumb_back/fh260/back_our/20200701/ourmid/pngtree-atmospheric-high-end-skin-care-product-advertising-background-image_344527.jpg" alt="" />
                        </a>
                    </div> */}
                </div>
            </div>
        </div>
    );
};

export default BigBanner;
