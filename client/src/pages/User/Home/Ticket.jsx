import { useEffect } from 'react';
import css from './home.module.scss';
import { ArrowUp } from 'react-bootstrap-icons';
function Ticket({ show }) {
    const scrollToTop = () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth',
        });
    };
    useEffect(() => {}, []);
    return (
        <div>
            {show && (
                <>
                 
                    
                    <button
                        className="rounded-full bg-blue-300 fixed right-4 bottom-4 h-14 w-14"
                        onClick={scrollToTop}
                    >
                        <ArrowUp />
                    </button>
                </>
            )}
        </div>
    );
}

export default Ticket;
