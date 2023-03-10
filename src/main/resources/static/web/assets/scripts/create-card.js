const { createApp } = Vue;

createApp({
    data() {
        return {
            data:null,
            cardType:"",
            cardColor:"",
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get(`http://localhost:8080/api/clients/current`)
            .then(res=>{
                this.data=res;
                this.openCarousel();
                // this.loadCharts();
            })
        },
        openNav() {
            let container=document.querySelector(".lateral-navigation-subcontainer");
            if (window.innerWidth>768){
                if (!container.style.minWidth||container.style.minWidth=="3rem"){
                    container.style.minWidth = "300px";
                }else{
                    container.style.minWidth = "3rem";
                }
            }else{
                if (!container.style.minHeight||container.style.minHeight=="3rem"){
                    container.style.minHeight = "300px";
                }else{
                    container.style.minHeight = "3rem";
                }
            }
        },
        sesionLogout(){
            axios.post('/api/logout').then(response => {
                console.log('signed out!!!')
                window.location.href = '/web/index.html';
            })
            
        },
        createCard(){
            axios.post("/api/clients/current/cards",`cardType=${this.cardType}&cardColor=${this.cardColor}`)
            .then(res=>{
                console.log("Card Created")
            })
            .catch(err=>{
                console.error(err.message)
            })
        },
    },
    computed:{
        hashUser(){
            return this.data.data.firstName[0]+this.data.data.lastName[0]+this.data.data.email;
        },
        
    }
}).mount('#app')