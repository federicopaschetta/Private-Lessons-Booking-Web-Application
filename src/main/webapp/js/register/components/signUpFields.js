
var signupscripts = Vue.component('signupscripts', {
    template:`
        <div class="signUpPage">
            <div class="signUpContainer">
                <img src="icons/writing1.png" class="signUpTitle">
                <div class="signUpForm">
                    <div id="signUpName" class="signUpFields">
                        <input v-model="signUpName" placeholder="Nome" type="text" required/>
                        <span></span>
                        <label>Nome</label>
                    </div>
                    <div id="signUpSurname" class="signUpFields">
                        <input v-model="signUpSurname" placeholder="Cognome" type="text" required/>
                        <span></span>
                        <label>Cognome</label>
                    </div>
                    <div id="signUpEmail" class="status signUpFields">
                        <input v-model="signUpEmail" placeholder="Email" type="text" v-on:focusout="emailCheck" required/>
                        <span></span>
                        <label>Email</label>
                        <div class="emailStatus">
                            <img v-if="emailState === true" src="icons/icon-done.svg">
                            <img v-if="emailState !== true && emailState!==null" src="icons/icon-warning.png">
                    </div>
                    </div>
                    
                    <div id="signUpUsername" class="status signUpFields">
                        <input v-model="signUpUsername" placeholder="Username" type="text" v-on:focusout="usernameCheck" required/>
                        <span></span>
                        <label>Username</label>
                        <div class="usernameStatus">
                            <img v-if="usernameState === true" src="icons/icon-done.svg">
                            <img v-if="usernameState !== true && usernameState!==null" src="icons/icon-warning.png">
                        </div>
                    </div>
                    <div id="signUpPassword" class="signUpFields">
                        <input v-model="signUpPassword" placeholder="Password" type="password" v-on:keyup="passwordProgress" required/>
                        <span></span>
                        <label>Password</label>
                    </div>
                    <div class="progress" style="width: 100%; height: 2px">
                            <div v-if="progress === 1" class="progress-bar bg-danger" role="progressbar" style="width: 25%" aria-valuenow="25" aria-valuemin="0" aria-valuemax="50"></div>
                            <div v-if="progress === 2" class="progress-bar bg-warning" role="progressbar" style="width: 50%" aria-valuenow="50" aria-valuemin="0" aria-valuemax="50"></div>
                            <div v-if="progress === 3" class="progress-bar bg-success" role="progressbar" style="width: 75%" aria-valuenow="75" aria-valuemin="0" aria-valuemax="50"></div>
                            <div v-if="progress === 4" class="progress-bar bg-success" role="progressbar" style="width: 100%" aria-valuenow="100" aria-valuemin="0" aria-valuemax="50"></div>
                    </div>
                    <div id="signUpConfirmPassword" class="status signUpFields">
                        <input v-model="signUpConfirmPassword" placeholder="Conferma Password" type="password" v-on:keyup="passwordCheck" required/>
                        <span></span>
                        <label>Conferma Password</label>
                        <div class="passwordStatus">
                            <img v-if="passwordState === true" src="icons/icon-done.svg">
                            <img v-if="passwordState !== true && passwordState!==null" src="icons/icon-warning.png">
                        </div>
                    </div>
                    <button v-on:click="signUpInner" class="signUpButton">Registrati</button>
                    <div v-if="result===true">L'operazione e' andata a buon fine</div>
                    <div v-if="result===false">L'operazione non e' andata a buon fine</div>
                </div>
            </div>
        </div>
    `,
    data: function () {
        return {
            signUpName: null,
            signUpSurname: null,
            signUpEmail: null,
            emailState: null,
            signUpUsername: null,
            usernameState: null,
            signUpPassword: null,
            signUpConfirmPassword: null,
            passwordState: null,
            progress: null,
            result: null,
            unique: null,
            link: 'ServletRegistra'
        }
    },
    methods: {
        signUpInner: function () {
            if((this.signUpName!==null)&&(this.signUpSurname!==null)&&(this.emailState===true) &&
                (this.usernameState===true) && (this.passwordState===true)) {
                this.$emit('sign-up-inner', this.signUpName, this.signUpSurname, this.signUpEmail,
                    this.signUpUsername, this.signUpPassword);
            } else {
                alert("Impossibile completare la registrazione. Uno o più campi non sono corretti!");
            }
        },
        emailValidation: function (emailAddress) {
            var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
            return(emailAddress.match(mailformat));
        },
        emailCheck: function () {
            if(this.emailValidation(this.signUpEmail)) {
                var self = this;
                $.get(this.link, {Action: 'UniqueEmail', mailAddress: this.signUpEmail}, function (data) {
                    self.emailState = data;
                });
            } else {
                this.emailState = 'Hai inserito un indirizzo email invalido!'
            }
        },
        usernameCheck: function () {
            if(this.signUpUsername.trim().length>0 && this.signUpUsername != null) {
                var self = this;
                $.get(this.link, {Action: 'UniqueUsername', username: this.signUpUsername}, function(data) {
                    self.usernameState = data;
                });
            } else {
                this.usernameState = false;
            }

        },
        passwordCheck: function () {
            var equal = this.signUpPassword === this.signUpConfirmPassword;
            var long = this.signUpPassword.length >= 8;
            if(equal) {
                if(long) {
                    this.passwordState = true;
                } else {
                    this.passwordState = 'La password deve contenere almeno 8 caratteri.';
                }
            } else {
                this.passwordState = 'Le due password non corrispondono.'
            }
        },
        passwordProgress: function () {
            var pwProgress = 0;
            if(this.signUpPassword != null) {
                if(/\d/.test(this.signUpPassword)) {
                    pwProgress++;
                }
                if(/[a-z]/.test(this.signUpPassword)) {
                    pwProgress++;
                }
                if(/[A-Z]/.test(this.signUpPassword)) {
                    pwProgress++;
                }
                if(/[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/.test(this.signUpPassword)) {
                    pwProgress++;
                }
            }
            this.progress = pwProgress;

        },
    }
});


var app = new Vue({
    data: {
        Name: null,
        linkSignUp: 'ServletRegistra',
        ruolo: null

    },
    el: '#authenticationSection',
    methods: {

        switchPage: function () {
            this.login = false;
        },
        signUpMethod: function (name, surname, email, username, password) {
            var self = this;
            $.post(this.linkSignUp, {SignUpName: name, SignUpSurname: surname, SignUpEmail: email, SignUpUsername: username,
                SignUpPassword: password}, function(data) {
                self.result = data;
                self.Name = data.Name;
                self.ruolo = data.Role;
                if(self.result === true) {
                    location.replace('/ServletTest_war_exploded/index.html');
                }
            });
        },
    }
});