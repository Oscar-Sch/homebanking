const { createApp } = Vue;

createApp({
    data() {
        return {
            data:null,
            clientId:1,
            cards:[],
            cardsDebit:[],
            cardsCredit:[],
            userAvatar:null,
            activeCard:null,
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get(`/api/clients/current`)
            .then(res=>{
                this.data=res;
                this.cards=res.data.cards;
                this.cardsCredit=this.cards.filter(card=> card.type==="CREDIT");
                this.cardsDebit=this.cards.filter(card=> card.type==="DEBIT");
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
        cardListClick(card){
            this.activeCard=card;
        },
        deleteCard(){
            axios.delete(`/api/clients/current/cards?cardNumber=${this.activeCard.number}`)
            .then(res=>{
                window.location.reload();
            })
        },
        checkDue(date){
            let now=new Date();
            return now.toISOString().split("T")[0]<date;
        },
        sesionLogout(){
            axios.post('/api/logout').then(response => {
                console.log('signed out!!!')
                window.location.href = '/web/index.html';
            })
            
        },
    },    
    computed:{
        hashUser(){
            return this.data.data.firstName[0]+this.data.data.lastName[0]+this.data.data.email;
        },
        
    }
}).mount('#app')