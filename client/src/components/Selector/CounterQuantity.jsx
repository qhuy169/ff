import { useState } from 'react';
import { ChevronDown, PlusLg, DashLg } from 'react-bootstrap-icons';
import swal from 'sweetalert';

function CounterQuantity({ onChange, value, sphientai }) {
    const [quantity, setQuantity] = useState(value);

    const handleChange = (e) => {

        let number = parseInt(e.target.value);
        if( number > sphientai){
            swal({text: 'Chỉ được mua tối đa sản phẩm hiện có!', icon: 'warning',});
        }else{
            if (Number.isInteger(number)) {
                setQuantity(number);
                onChange(number);
            }
        }
       
    };
    return (
        <div className="flex items-center">
            <span
                className="select-none cursor-pointer p-6 text-blue-400 h-16 flex items-center"
                style={quantity === 1 ? { color: '#eee' } : {}}
                onClick={() => {
                    if (quantity === 1) return;
                    setQuantity((old) => old - 1);
                    onChange(quantity - 1);
                }}
            >
                <i>
                    <DashLg />
                </i>
            </span>
            <input
                type="number"
                min="1"
                max="1000"
                onChange={handleChange}
                value={quantity}
                name="form-0-quantity"
                className="h-16 w-20 outline-none text-center text-xl border-inset"
            />
            <span
                className="select-none cursor-pointer p-6 text-blue-400 h-16 flex items-center"
                onClick={() => {
                    setQuantity((old) => old + 1);
                    onChange(quantity + 1);
                }}
            >
                <i>
                    <PlusLg />
                </i>
            </span>
        </div>
    );
}

export default CounterQuantity;
