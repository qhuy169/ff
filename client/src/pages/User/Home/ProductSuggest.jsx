import { useState } from 'react';
import ProductCard from '~/components/ProductCard';
import Section from './Section';
import { useSelector } from 'react-redux';

function ProductSuggest() {
    const [limit, setLimit] = useState(10);
    const number = 25;

    // Lấy dữ liệu từ Redux Store
    const productsData = useSelector((state) => state.products?.pageProduct?.data);
    const products = Array.isArray(productsData?.content) ? productsData.content : [];

    const handleClick = () => {
        setLimit(number);
    };

    return (
        <Section title="SẢN PHẨM MỚI" styles="bg-white">
            <div className="flex flex-wrap gap-8 w-full">
                {products.length > 0 ? (
                    products.slice(0, limit).map((product) => (
                        <ProductCard key={product.title} {...product} soldout={product.availableQuantity === 0} />
                    ))
                ) : (
                    <p className="text-center w-full">Không có sản phẩm nào.</p>
                )}

                <div className="w-full">
                    {limit !== number && (
                        <button
                            className="m-auto block bg-white rounded-lg px-56 py-4 border outline-none"
                            onClick={handleClick}
                        >
                            {'Xem thêm'}
                        </button>
                    )}
                </div>
            </div>
        </Section>
    );
}

export default ProductSuggest;
