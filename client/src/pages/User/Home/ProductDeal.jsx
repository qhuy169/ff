import Section from './Section';
import css from './home.module.scss';
import { Link } from 'react-router-dom';
function ProductDeal() {
    const images = [
        'https://png.pngtree.com/png-clipart/20191120/original/pngtree-sale-discount-banner-special-offer-png-image_5008261.jpg',
        'https://png.pngtree.com/png-clipart/20200625/ourlarge/pngtree-super-offer-big-sale-50-off-banner-png-image_2263650.jpg',
        'https://img.lovepik.com/photo/50103/4953.jpg_wh860.jpg'
        ,
    ];
    return (
        <Section title="THÔNG TIN ƯU ĐÃI">
            <>
                {images.map((src, index) => {
                    return (
                        <Link className={css.cover} key={index} to='#'>
                            <img src={src} className="w-full h-full"/>
                        </Link>
                    );
                })}
            </>
        </Section>
    );
}

export default ProductDeal;
