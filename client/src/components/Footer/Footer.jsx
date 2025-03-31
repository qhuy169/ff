import { Facebook, Youtube } from 'react-bootstrap-icons';
import styles from './footer.module.scss';
function Footer() {
    return (
        <footer className={styles.footer}>
            <div className={styles.top}>
                <div className={styles.wrap}>
                    <ul>
                    <li className="footer__item">
                            <a href="" className="footer__item-link">Trung tâm trợ giúp</a>
                        </li>
                        <li className="footer__item">
                            <a href="" className="footer__item-link">PhúcXiCuc-shop mall</a>
                        </li>
                        <li className="footer__item">
                            <a href="" className="footer__item-link">Hướng dẫn mua hàng</a>
                        </li>
                        <li>Chính sách bảo hành</li>
                        <li>Xem thêm</li>
                    </ul>
         
                    <ul>
                        <li>Tổng đài hỗ trợ (Miễn phí gọi)</li>
                        <li>Gọi mua: 1800.1060 (7:30 - 22:00)</li>
                        <li>Kỹ thuật: 1800.1763 (7:30 - 22:00)</li>
                        <li>Khiếu nại: 1800.1062 (8:00 - 21:30)</li>
                        <li>Bảo hành: 1800.1064 (8:00 - 21:00)</li>
                    </ul>
                   
                </div>
            </div>
            <div className={styles.bottom}>
                <p>
                © 2022 - Bản quyền thuộc về Công ty PNTech
                </p>
            </div>
        </footer>
    );
}

export default Footer;
