const { createApp } = Vue;

createApp({
    data() {
        return {
            email:null,
            password:null,
            registerName:null,
            registerLastName:null,
            registerUserName:null,
            registerEmail:null,
            registerPhoneNumber:null,
            registerPassword:null,
            loginErrMsg:"",
            registerErrMsg:"",
        }
    },
    created(){
        // this.loadData();
    },
    mounted(){
        const hexagons=document.querySelectorAll(".hexagon");
        hexagons.forEach(hex=>{
        hex.style.animationDelay= `${Math.random()*5}s`;
        console.log("first")
        this.observerInit();
})
    },
    methods:{
        sesionLogin(email, pass){
            axios.post('/api/login',`email=${email}&password=${pass}`,
                    {headers:{'content-type':'application/x-www-form-urlencoded'}})
                    .then(response => {
                        console.log('signed in!!!');
                        window.location.href = '/web/accounts.html';
                    })
                    .catch(err=>{
                        this.loginErrMsg="Incorrect user or password. Please try again.";
                    })
        },
        sesionLogout(){
            axios.post('/api/logout').then(response => console.log('signed out!!!'))
        
        },
        sesionRegister(){
            axios.post('/api/clients',
            `firstName=${this.registerName}&lastName=${this.registerLastName}&email=${this.registerEmail}&password=${this.registerPassword}&userName=${this.registerUserName}&phoneNumber=${this.registerPhoneNumber}`,{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => {
                console.log('registered')
                this.sesionLogin(this.registerEmail, this.registerPassword)
            })
            .catch(err=>{
                this.registerErrMsg=err.response.data;
            })
        },
        singIn(){
            const display=document.querySelector(".modal-body");
            display.style.animation="msg-display-start 1.5s forwards,section-open-glitch 1.5s forwards,   section-open-glitch-shadow 1.5s forwards";
        },
        singUpTransition(){
            const display=document.querySelector(".modal-body");
            const container=document.querySelector(".login-register-container");
            display.style.animation="msg-display-start 1s forwards,section-open-glitch 1s forwards,   section-open-glitch-shadow 1s forwards,bg-glitch 2s";
            container.style.animation="register-slide .2s forwards";
        },
        singUpTransitionClear(){
            setTimeout(() => {
                const display=document.querySelector(".modal-body");
                const container=document.querySelector(".login-register-container");
                display.style.animation="none";
                container.style.animation="none";
            }, 500);
        },
        observerInit(){
            const msgPads=document.querySelectorAll(".message-pad");
            const observer = new IntersectionObserver(entries => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        if(entry.target.childNodes[0].childNodes[2].classList.value.includes("message-pad_charge-bar-right")){
                            entry.target.childNodes[0].childNodes[2].classList.add("msg-bar-start-right");
                        }else{
                            entry.target.childNodes[0].childNodes[2].classList.add("msg-bar-start");
                        }
                        if(entry.target.childNodes[0].childNodes[3].childNodes[0].classList.value.includes("message-pad_light-container-right")){
                            entry.target.childNodes[0].childNodes[3].childNodes[0].classList.add("msg-lights-rotation-start-right");
                        }else{
                            entry.target.childNodes[0].childNodes[3].childNodes[0].classList.add("msg-lights-rotation-start");
                        }
                        entry.target.childNodes[0].childNodes[3].childNodes[0].childNodes.forEach(light=>{
                            light.classList.add("msg-light-start");
                        })
                        entry.target.childNodes[1].childNodes[0].classList.add("msg-arm-start");
                        entry.target.childNodes[1].childNodes[0].childNodes[0].classList.add("msg-display-start");
                        observer.unobserve(entry.target);
                    }
                });
            },
            { threshold: 1 });
            msgPads.forEach(pad=>{
                observer.observe(pad);
            })
        }
    },
    computed:{
    }
}).mount('#app')