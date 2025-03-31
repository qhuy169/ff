import { Link } from 'react-router-dom';
import { CartX } from 'react-bootstrap-icons';
function EmptyCart() {
    return (
        <div className="flex items-center justify-center flex-col h-[45vh] gap-6 bg-white">
            <p>Không có sản phẩm nào trong giỏ hàng</p>
            <i>
                <CartX className="text-9xl text-orange-400" />
            </i>
            
            <Link to="/">
                <button className=" bg-transparent text-blue-700 font-semibold py-1 px-24 border border-blue-500 rounded-lg uppercase h-20">
                    Tiếp tục mua sắm
                </button>
            </Link>
        </div>
    );
}

export default EmptyCart;
