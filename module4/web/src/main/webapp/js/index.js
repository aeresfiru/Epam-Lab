(function () {
    const loadPage = async (page, size) => {
        const API_URL = `http://localhost:8088/api/v1/certificates?page=${page}&size=${size}`;

        await fetch(API_URL)
            .then((response) => {
                return response.json();
            })
            .catch((err) => console.log(err))
            .then((data) => {
                if (data) {
                    localStorage.setItem(`certificates`, data);
                    console.log(data._embedded)
                    localStorage.getItem("certificates").forEach(c => {
                        console.log(c);
                    })
                    showCertificates();
                }
            })
    }

    const showCertificates = () => {
        const items = document.getElementsByClassName('list-items')[0];
        const certificates = localStorage.getItem("certificates");

        /*     const coupon = document.createElement("div");
             coupon.classList.add('coupon');
             coupon.innerHTML = `
         <div class="coupon-frame">
         </div>
         <div class="coupon-properties">
             <div class="coupon-property">
                 <div class="coupon-name">
                     ${certificate.name}
                     <span class="material-icons favorite-icon">favorite</span>
                 </div>
                 <div class="coupon-description">
                         Some brief description
                     <span>Expires in 3 days</span>
                 </div>
             </div>
             <hr>
             <div class="coupon-price">
                 <div style="display: flex; align-items: center">
                     <span class="material-icons">attach_money</span><span style="font-size: larger">235</span>
                 </div>
                 <button type="button" class="add-cart-button">Add to cart</button>
             </div>
         </div>
         `
             items.appendChild(coupon);*/
        //coupon.appendChild(coupon);
    }

    var isExecuted = false;

    const infiniteScroll = () => {
        if (window.scrollY > (document.body.offsetHeight - window.outerHeight) && !isExecuted) {
            isExecuted = true;

            //code...
            console.log("Working...");

            setTimeout(() => {
                isExecuted = false;
            }, 1000);
        }
    }
    window.onscroll = infiniteScroll;

    let currentPage = 1;
    const size = 10;
    let total = 0;

    loadPage(currentPage, size);
})();