var navbarGuest = Vue.component('navbar-guest', {
    template:`
        <nav>
            <a href="#"> <img src="icons/writing2.png" alt="RipetiAmo"> </a>
            <div class="nav-links" id="navLinks">
                <ul>
                    <li> <a href="#courses" class="navBarLinks">I nostri corsi</a></li>
                    <li> <a href="#teachers" class="navBarLinks">I nostri docenti</a></li>
                    <li> <button v-on:click="switchToLogin" class="hero-btn">Accedi</button></li>
                </ul>
            </div>
        </nav>
    `,
    methods: {
        switchToLogin: function () {
            this.$emit('switch-login');
        },
        toggleMenu: function (id) {
            if(id === 'hamburgerMenu') {
                const elemactive = $.getElementById('hamburgerMenu');
                elemactive.classList.removeClass('active');
                const elemnotactive = $.getElementById('closeMenu');
                elemnotactive.classList.addClass('active')
            } else {
                const elemactive = $.getElementById('closeMenu');
                elem.classList.removeClass('active');
                const elemnotactive = $.getElementById('hamburgerMenu');
                elemnotactive.classList.addClass('active')
            }

        }

    }
});

var navbarAdmin = Vue.component('navbar-admin', {
    template:`
        <nav class="navAdmin">
            <a href="#"> <img src="icons/writing2.png" alt="RipetiAmo"> </a>
            <div class="nav-links">
                <ul>
                    <li id="welcomeUser"> 
                        <a hef="#" class="navBarLinks" id="WelcomeUser" v-on:click="logoutOn">Ciao {{Username}}<img src="icons/user.png"></a></li>
                </ul>
                <button v-if="logoutView" class="userDropDownListBtn" v-on:click="logoutInner">Logout</button>
                <i class="fa-solid fa-xmark-large" onclick="hideMenu()"></i>
            </div>
        </nav>
    `,
    props:['Username'],
    data: function () {
        return {
            logoutView: false
        }
    },
    methods: {
        logoutOn: function () {
            this.logoutView = !this.logoutView;
        },
        logoutInner: function () {
            this.$emit('logout-inner');
            console.log("NavBarAdmin");
        }
    }
});


var heroaction = Vue.component('heroAction', {
    template: `
        <div class="textbox">
            <h1>L'amore per il <span>sapere</span> <br>
               arriva a casa tua.</h1>
            <p>Con Ripetiamo puoi riscoprire la tua <span>passione per la cultura</span> con insegnanti privati certificati.</p>
            <button class="hero-btn" id="booking" v-on:click="switchLogin"> Prenota una lezione </button>
        </div>
    `,
    methods: {
        switchLogin: function () {
            this.$emit('switch-login');
        }
    }
});

var bookLesson = Vue.component('book-lesson', {
    template:`
        <section class="BaL" id="BaL">
            <h2>Non perdere <span>tempo</span></h2>
            <h3><span>Impara</span> anche tu</h3>
                <div class="dropDown" id="bookingLesson">
                    <div class="choice" id="choice">
                        <div class="scelta corso">
                            <label for="corso">Corso</label>
                            <select name="corso" id="corso" v-model="selectedCourse">
                                <option v-for="course in coursesList">{{course.Title}}</option>
                            </select>
                        </div>

                        <div class="scelta docente">
                            <label for="docente">Docente</label>
                            <select id="docente" name="Docente" v-model="selectedTeacher">
                                <option disabled value=""></option>
                                <option v-for="teacher in teachersList">{{teacher.Surname}}, {{teacher.Name}} </option>
                            </select>
                        </div>


                        <div class="scelta giorno">
                            <label for="giorno">Giorno</label>
                            <select id="giorno" name="Giorno" v-model="selectedDay">
                                <option disabled value=""></option>
                                <option v-for="day in daysList">{{day.day}}</option>
                            </select>
                        </div>

                        <div class="scelta ora">
                            <label for="ora">Ora</label>
                            <select id="ora" name="Ora" v-model="selectedHour">
                                <option disabled value=""></option>
                                <option v-for="hour in hoursList">{{hour.time}}</option>
                            </select>
                        </div>
                    </div>
                    <button class="hero-btn" v-on:click="bookLesson">Prenota</button>
                </div>
                <div class="bookingResultSection" v-if="result === true">
                        <img src="icons/icon-done.svg" alt="Prenotazione effettuata">
                        <h4>La tua prenotazione e' andata a buon fine.</h4>
                    </div>
                    <div class="bookingResultSection" v-if="result === false">
                        <img src="icons/icon-warning.png" alt="Prenotazione non effettuata">
                        <h4>La tua prenotazione non e' andata a buon fine.</h4>
                    </div>
        </section>
    `,
    data: function() {
        return {
            coursesList:[],
            teachersList:[],
            daysList:[{day: 'Lunedi'}, {day: 'Martedi'}, {day: 'Mercoledi'}, {day: 'Giovedi'}, {day: 'Venerdi'}],
            hoursList: [],
            selectedCourse: null,
            selectedTeacher: null,
            selectedDay: null,
            selectedHour: null,
            result: null,
            link: 'ServletEffettuaPrenotazioni'
        }
    },
    mounted() {
        this.viewAvailableCourses();
    },
    methods: {
        viewAvailableCourses: function() {
            var self = this;
            $.post(this.link, {Action:'AvailableCourses'}, function(data) {
                self.coursesList = data;
            });
        },
        viewAvailableTeachers: function() {
            var self = this;
            $.post(this.link, {Action: 'AvailableTeachers', Course: self.selectedCourse}, function(data) {
                self.teachersList = data;
            });
        },
        viewAvailableDays: function() {
            var self = this;
            $.post(this.link, {Action: 'AvailableDays', Course: self.selectedCourse, Teacher: self.selectedTeacher,
            }, function(data) {
                self.daysList = data;
            });
        },
        viewAvailableHours: function() {
            var self = this;
            $.post(this.link, {Action: 'AvailableHours', Course: self.selectedCourse, Teacher: self.selectedTeacher,
                Day: self.selectedDay}, function(data) {
                self.hoursList = data;
            });
        },
        bookLesson: function() {
            result = 'true';
            var self = this;
            $.post(this.link, {Action:'BookLesson', Course: self.selectedCourse, Teacher: self.selectedTeacher, Day: self.selectedDay,
                Hour: self.selectedHour}, function(data) {
                self.result = data;
            });
        }
    },
    watch: {
        selectedCourse: function() {
            var self = this;
            $.post(this.link, {Action: 'AvailableTeachers', Course: self.selectedCourse}, function(data) {
                self.teachersList = data;
            });
        },
        selectedDay: function() {
            var self = this;
            $.post(this.link, {Action: 'AvailableHours', Course: self.selectedCourse, Teacher: self.selectedTeacher,
                Day: self.selectedDay}, function(data) {
                self.hoursList = data;
            });
        }
    }
});

var viewCoursesGuest = Vue.component('view-courses-section', {
    template:`
    <div class="campus" id="courses">
        <h2>I nostri <span>insegnamenti</span> pi&ugrave; richiesti</h2>
        <div class="campus-row">
            <div class="campus-col">
                <img src="immagini/letteratura.jpg" alt="Literature Course">
                <div class="layer">
                    <h3>Letteratura</h3>
                </div>
            </div>
            <div class="campus-col">
                <img src="immagini/matematica.jpg" alt="Math Course">
                <div class="layer">
                    <h3>Matematica</h3>
                </div>
            </div>
            <div class="campus-col" id="geo">
                <img src="immagini/geografia.jpg" alt="Geography Course">
                <div class="layer">
                    <h3>Geografia</h3>
                </div>
            </div>
        </div>
        <button class="hero-btn" v-if="coursesViewed === false" v-on:click="viewAllCourses"> Visualizza i corsi </button>
        <button class="hero-btn" v-if="coursesViewed === true" v-on:click="hideAllCourses"> Nascondi i corsi </button>
        <div v-if="coursesViewed === true" id="viewCourses" class="tableSection">  
            <table class="coursesTable dataTable">
                <tr v-if="coursesViewed === true">
                    <th>Titolo</th>
                </tr>
                <tr v-if="coursesViewed===true" v-for="course in coursesList" class="lessonsRow">
                    <td>{{course.Title}}</td>
                </tr>
            </table>
        </div>
    </div>
    `,
    data: function () {
        return {
            coursesViewed: false,
            coursesList:[],
            link: 'ServletCorsi'
        }
    },
    methods:{
        viewAllCourses: function(){
            this.coursesViewed = true;
            var self = this;
            $.get(this.link, function(data) {
                self.coursesList = data;
            });
        },
        hideAllCourses: function () {
            this.coursesViewed = false;
        }
    }
});

var viewTeachersGuest = Vue.component('view-teachers-section', {
    template:`
        <section class="teachers" id="teachers">
        <h2><span>Professionisti</span> al vostro servizio</h2>
        <p>I nostri <span>docenti</span> sono attentamente selezionati per fornire
            un insegnamento di <span>alta qualit&agrave;</span>.</p>
        <div class="container">
            <div class="card">
                <div class="item front-side">
                    <img src="immagini/avatar/giovanni.jpg" alt="Giovanni profile picture">
                        <h4>Giovanni, <span>32</span></h4>
                </div>
                <div class="item back-side">
                        <h4>Giovanni, 32</h4>
                        <ul>
                            <li>Laurea in Ingegneria Informatica (99/110)</li>
                            <li>Studente di Data Science</li>
                        </ul>
                </div>

            </div>
            <div class="card">
                <div class="item front-side">
                    <img src="immagini/avatar/luisa.jpg" alt="Luisa profile picture">
                        <h4>Luisa, <span>27</span></h4>
                </div>
                <div class="item back-side">
                    <h4>Luisa, 27</h4>
                    <ul>
                        <li>Laurea in Lettere Classiche </li>
                        <li>Cattedra di latino e greco</li>
                    </ul>
                </div>
            </div>

            <div class="card">
                <div class="item front-side">
                    <img src="immagini/avatar/luca.jpg" alt="Giulia profile picture">
                        <h4>Luca, <span>25</span></h4>
                </div>
                <div class="item back-side">
                    <h4>Luca, 25</h4>
                    <ul>
                        <li>Diploma di Liceo Classico con 100/100 e lode</li>
                        <li>Laureando in Medicina</li>
                    </ul>
                </div>
            
            </div>
            <div class="card">
                <div class="item front-side">
                    <img src="immagini/avatar/giulia.jfif" alt="Giulia profile picture">
                        <h4>Giulia, <span>49</span></h4>
                </div>
                <div class="item back-side">
                    <h4>Giulia, 49</h4>
                    <ul>
                        <li>Laurea in Giurisprudenza (110/110)</li>
                        <li>Avvocato penalista dal 2001</li>
                    </ul>
                </div>
            </div>
        </div>

            <div id="viewAllTeachers">
                <button class="hero-btn viewTeachers" v-if="!teachersViewed" v-on:click="viewAllTeachersFunction"> Visualizza i docenti </button>
                <button class="hero-btn viewTeachers" v-if="teachersViewed === true"  v-on:click="hideAllTeachersFunction"> Nascondi i docenti </button>
                <div v-if="teachersViewed" id="viewTeachersGuest" class="tableSection">
                    <table id="teachersTable" class="dataTable">
                        <tr v-if="teachersViewed === true">
                            <th>Nome</th>
                            <th>Cognome</th>
                        </tr>
                        <tr v-if="teachersViewed===true" v-for="teacher in teachersList" class="lessonsRow">
                            <td>{{teacher.Name}}</td>
                            <td>{{teacher.Surname}}</td>
                        </tr>
                    </table>
                </div>
            </div>
    </section>
    `,
    data: function () {
        return {
            teachersViewed: false,
            teachersList:[],
            link: 'ServletDocenti'
        }
    },
    methods:{
        viewAllTeachersFunction: function(){
            this.teachersViewed = true;
            var self = this;
            $.get(this.link, function(data) {
                self.teachersList = data;
            });
        },
        hideAllTeachersFunction: function(){
            this.teachersViewed = false;
        }
    }
});

var reviews = Vue.component('reviews-section', {
    template: `
        <section class="reviews">
        <h2>Dicono di <span>noi</span></h2>
        <p>Ripetiamo &egrave; <span>leader</span> in Italia nel settore.</p>
        <p>Il 99% dei nostri studenti rimane <span>soddisfatta</span> del nostro servizio.</p>
        <div class="reviews-row">
            <div class="reviews-col">
                <div class="review-comment">
                    <img class="openQuotes" src="icons/openQuotes.png" alt="openQuotes">
                    <p>Me ne hanno parlato molti miei studenti. <br>Visti i loro <span>risultati</span>, inizier&ograve;
                    sicuramente a far usare <span>Ripetiamo</span> ai miei figli.</p>
                    <img class="closeQuotes" src="icons/closeQuotes.png" alt="openQuotes">
                </div>
                <div class="reviews-source-rating">
                    <h3>Lucia, professoressa</h3>
                    <div class="stars">
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                    </div>
                </div>
            </div>
            <div class="reviews-col">
                <div class="review-comment">
                    <img class="openQuotes" src="icons/openQuotes.png" alt="openQuotes">
                    <p>Questa &egrave; una grande <span>innovazione</span>. Gli studenti di ogni grado potranno
                        <span>colmare</span> le loro lacune seguiti individualmente da esperti. 
                    </p>
                    <img class="closeQuotes" src="icons/closeQuotes.png" alt="openQuotes">
                </div>
                <div class="reviews-source-rating">
                    <h3>"Corriere della Sera"</h3>
                    <div class="stars">
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                    </div>
                </div>
            </div>
        </div>

        <div class="reviews-row">
            <div class="reviews-col">
                <div class="review-comment">
                    <img class="openQuotes" src="icons/openQuotes.png" alt="openQuotes">
                    <p>&Egrave; un'<span>incredibile</span> piattaforma, non pu&ograve; che avere un'impatto
                    <span>enorme</span> sulle nuove generazioni.</p>
                    <img class="closeQuotes" src="icons/closeQuotes.png" alt="openQuotes">
                </div>
                <div class="reviews-source-rating">
                    <h3>"La Stampa"</h3>
                    <div class="stars">
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                    </div>
                </div>
            </div>
            <div class="reviews-col">
                <div class="review-comment">
                    <img class="openQuotes" src="icons/openQuotes.png" alt="openQuotes">
                    <p>Devo ringraziare un mio amico che me l'ha fatto <span>scoprire</span>. Da quando ho conosciuto
                        <span>Ripetiamo</span> non ho pi&ugrave; avuto paura di nessuna interrogazione a sorpresa.</p>
                    <img class="closeQuotes" src="icons/closeQuotes.png" alt="openQuotes">
                </div>
                <div class="reviews-source-rating">
                    <h3>Antonio, 16 anni</h3>
                    <div class="stars">
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                        <i class="fa fa-solid fa-star"></i>
                    </div>
                </div>
            </div>
        </div>
    </section>
    `,
});

var sidebar = Vue.component('side-bar', {
    template: `
        <div class="clientSection">
            <div class="sidebar">
                <img src="icons/writing2.png" class="sideLogo">
                <ul class="functionsList">
                    <li>
                        <button class="clientFunctionsBtn unselectedClientBtn"  id="logoutClient" v-on:click="switchClientView('logoutClient')">
                            <img v-if="this.active !== 'logoutClient'" src="icons/user.png" alt="Logout">
                            <img v-if="this.active === 'logoutClient'" src="icons/user-on.png" alt="Logout">
                            <h3> Ciao {{Username}}</h3>
                        </button>
                    </li>
                    <li>
                        <button class="clientFunctionsBtn selectedClientBtn"  id="BaL" v-on:click="switchClientView('BaL')">
                            <img v-if="this.active !== 'BaL'" src="icons/calendar.png" alt="Prenota una lezione">
                            <img v-if="this.active === 'BaL'" src="icons/calendar-on.png" alt="Prenota una lezione">
                            <h3>Prenota</h3>
                        </button>
                    </li>
                    <li>
                        <button class="clientFunctionsBtn unselectedClientBtn"  id="myBookings"  v-on:click="switchClientView('myBookings')">
                            <img v-if="this.active!=='myBookings'" src="icons/appointment.png" alt="Le tue prenotazioni">
                            <img v-if="this.active==='myBookings'" src="icons/appointment-on.png" alt="Le tue prenotazioni">
                            <h3>Le tue prenotazioni</h3>
                        </button>
                    </li>
                    <li>
                        <button class="clientFunctionsBtn unselectedClientBtn"  id="ourLessons" v-on:click="switchClientView('ourLessons')">
                        <img v-if="this.active!=='ourLessons'" src="icons/book.png" alt="Le nostre lezioni">
                        <img v-if="this.active==='ourLessons'" src="icons/book-on.png" alt="Le nostre lezioni">
                        <h3>Le nostre lezioni</h3>
                        </button>
                    </li>
                    <li>
                        <button class="clientFunctionsBtn unselectedClientBtn"  id="contactUs" v-on:click="switchClientView('contactUs')">
                            <img v-if="this.active !== 'contactUs'" src="icons/envelope.png" alt="Contattaci">
                            <img v-if="this.active === 'contactUs'" src="icons/envelope-on.png" alt="Contattaci">
                            <h3>Contattaci</h3>
                        </button>
                    </li>
                </ul>
            </div>
        </div>
    `,
    props:['Username'],
    data: function () {
        return {
            active: 'BaL'
        }
    },
    methods: {
        switchClientView(section) {
            $('#'.concat(this.active)).removeClass("selectedClientBtn");
            $('#'.concat(this.active)).addClass("unselectedClientBtn");
            this.active = section;
            $('#'.concat(section)).removeClass("unselectedClientBtn");
            $('#'.concat(section)).addClass("selectedClientBtn");
            this.$emit('switch-client-view', section);
        },
    }
});

var viewAvailableLessons = Vue.component('view-available-lessons', {
    template:`
    <div class="containerAvailableLessons">
        <h2>Le nostre <span>lezioni</span> disponibili</h2>
        <div id="viewAvailableLessons" class="tableSection">
        <table id="availableLessonsTable" class="dataTable">
            <tr>
                <th>Corso</th>
                <th>Nome Docente</th>
                <th>Cognome Docente</th>
                <th>Giorno</th>
                <th>Ora</th>
            </tr>
            <tr v-for="lesson in availableLessonsList" class="lessonsRow">
                <td>{{lesson.course}}</td>
                <td>{{lesson.teacherName}}</td>
                <td>{{lesson.teacherSurname}}</td>
                <td>{{lesson.day}}</td>
                <td class="lessonTime">{{lesson.time}}</td>
            </tr>
        </table>
    </div>
    </div>
    `,
    data: function () {
        return {
            availableLessonsList: [],
            link: 'ServletLezioni'
        }
    },
    mounted() {
        this.getAvailableLessonsList();
    } ,
    methods: {
        getAvailableLessonsList: function () {
            var self = this;
            $.get(this.link, {}, function (data) {
                self.availableLessonsList = data;
            });
        }
    }
});

var viewBookingsSection = Vue.component('view-booking-section',{
    template:`
        <div id="viewBookingsSection">
           <div v-if="empty === false">
                <table id="personalBookingsTable" class="dataTable viewBookings">
                <tr>
                    <th></th>
                    <th>Corso</th>
                    <th>Nome Docente</th>
                    <th>Cognome Docente</th>
                    <th>Giorno</th>
                    <th>Ora</th>
                    <th class="stateData">Stato</th>
                </tr>
                <tr v-for="lesson in lessonsList" :class="isEqual(lesson, selectedLesson) ? 'selected' : ''">
                        <td><input type="radio" name="lesson" @click="activate({ID: lesson.ID, course: lesson.course, teacherName: lesson.teacherName,
                        teacherSurname: lesson.teacherSurname, day: lesson.day, time: lesson.time, state: lesson.state})"></td>
                        <td>{{lesson.course}}</td>
                        <td>{{lesson.teacherName}}</td>
                        <td>{{lesson.teacherSurname}}</td>
                        <td>{{lesson.day}}</td>
                        <td>{{lesson.time}}</td>
                        <td v-if="lesson.state==='attiva'" class="activeBooking stateData"><img src="icons/yellow_light.png" class="semaphor" alt="Pending booking"/>
                            <button class="hero-btn changeStateBtn" id="initialDoneButton" v-on:click="markAs('Done')"><img class="changeState" src="icons/icon-done.svg"></button>
                            <button class="hero-btn changeStateBtn" id="initialCancelButton"  v-on:click="markAs('Canceled')"><img class="changeState" src="icons/icon-delete.png"></button>
                        </td>
                        <td v-else-if="lesson.state==='disdetta'"><img src="icons/red_light.png" class="semaphor" alt="Cancelled booking"/></td>
                        <td v-else-if="lesson.state==='effettuata'"><img src="icons/green-light.png" class="semaphor" alt="Done lesson"/></td>
                </tr>
            </table>
          </div>
          <div v-if="empty" class="viewLessonsEmpty">
            <h2>Non hai ancora <span>prenotato</span> una lezione</h2>
            <h3><span>Prenotala </span>subito!</h3>
          </div>
        </div>
    `,
    data: function () {
        return {
            lessonsList:[],
            selectedLesson: null,
            highlighted: false,
            link: 'ServletVisualizzaPrenotazioni',
            empty: null,
            result: null,
            course: null
        }
    },
    mounted() {
        this.showBookings();
    },
    methods:{
        showBookings: function(){
            this.tableViewed = true;
            var self = this;
            $.get(this.link, function(data) {
                self.lessonsList = data;
                self.empty = (typeof(data)!=="object");
            });
        },
        activate:function(el){
            if(this.isEqual(el, this.selectedLesson)) {
                this.highlighted = false;
            } else {
                this.selectedLesson = el;
                this.highlighted = true;
            }
        },
        isEqual: function(el1, el2) {
            return JSON.stringify(el1)===JSON.stringify(el2);
        },
        markAs(action) {
            var self = this;
            $.post(this.link, {Action:action, Course: this.selectedLesson.course, TeacherName: this.selectedLesson.teacherName,
                TeacherSurname: this.selectedLesson.teacherSurname, Day: this.selectedLesson.day, Time: this.selectedLesson.time}, function (data) {
                self.result = data;
                if(self.result) {
                    self.showBookings();
                }
            });
        }
    }
});

var logoutClient = Vue.component('logout-client', {
   template: `
    <div class="ClientFunction">
        <h2>Vuoi <span>disconnetterti</span> dal tuo profilo?</h2>
        <div>
            <button class="hero-btn logout" v-on:click="logoutInner">Logout</button>
        </div>
    </div>
   `,
    methods: {
        logoutInner: function () {
            this.$emit('logout-inner');
        }
    }
});

var contactUs = Vue.component('contact-us', {
    template: `
       <div class="ClientFunction">
            <h2>Hai <span>problemi</span> con il sito?</h2>
            <h2>Hai bisogno d'<span>aiuto</span>?</h2>
            <div>
                <button class="hero-btn client" v-on:click="contactInner">Contattaci</button>
            </div>
        </div> 
    `,
    methods: {
        contactInner: function () {
            this.$emit('contact-inner');
        }
    }
});


var adminViewBookings = Vue.component('admin-view-bookings',{
    template:`
        <div id="adminViewBookings">
            <div id="viewBookingsActive" class="adminManageSection">
                <button id="viewBookingsActive" class="manageButton"  v-on:click="toggleBookings">
                    <img src="icons/booking.png" alt="Visualizza prenotazioni"></button>
                <table class="dataTable" id="adminViewTable">
                    <tr v-if="viewBookingsAdmin">
                        <th></th>
                        <th>ID</th>
                        <th>Corso</th>
                        <th>Docente</th>
                        <th>Utente</th>
                        <th>Giorno</th>
                        <th>Ora</th>
                        <th>Stato</th>
                    </tr>
                    <tr v-if="viewBookingsAdmin" v-for="booking in bookingsList" :class="(selectedBooking===booking.ID) ? 'selected' : ''">
                        <td><input type="radio" name="booking" @click="activate(booking.ID)"></td>
                        <td>{{booking.ID}}</td>
                        <td>{{booking.course}}</td>
                        <td>{{booking.teacher}}</td>
                        <td>{{booking.user}}</td>
                        <td>{{booking.day}}</td>
                        <td>{{booking.time}}</td>
                        <td v-if="booking.state==='attiva'" class="activeBooking stateData"><img src="icons/yellow_light.png" class="semaphor" alt="Pending booking"/>
                            <button class="hero-btn changeStateBtn" id="initialDoneButton" v-on:click="markAs('Done')"><img class="changeState" src="icons/icon-done.svg"></button>
                            <button class="hero-btn changeStateBtn" id="initialCancelButton"  v-on:click="markAs('Canceled')"><img class="changeState" src="icons/icon-delete.png"></button>
                        </td>
                        <td v-else-if="booking.state==='disdetta'"><img src="icons/red_light.png" class="semaphor" alt="Cancelled booking"/></td>
                        <td v-else-if="booking.state==='effettuata'"><img src="icons/green-light.png" class="semaphor" alt="Done lesson"/></td>
                   </tr>
                </table>
            </div>
        </div>
    `,
    data: function () {
        return {
            viewBookingsActive: false,
            viewBookingsAdmin: false,
            bookingsList: [],
            selectedBooking: null,
            result: null,
            link: 'ServletAdmin'
        }
    },
    methods: {
        activateViewBookings: function () {
            this.viewBookingsActive = !this.viewBookingsActive;
        },

        toggleBookings: function() {
            if (!this.viewBookingsAdmin) {
                this.result = null;
                this.viewBookingsAdmin = true;
                this.showBookings();
            } else {
                this.viewBookingsAdmin = false;
            }
        },
        showBookings: function () {
            var self = this;
            $.get(this.link, {Action: 'ViewAllBookings'}, function(data) {
                self.bookingsList = data;
            });
        },
        activate: function (el) {
            this.selectedBooking = el;
        },
        markAs(action) {
            var self = this;
            $.post(this.link, {Action:action, BookingID:this.selectedBooking}, function (data)  {
               self.result = data;
               if(self.result) {
                   self.showBookings();
               }
            });
        }

    }
});


var adminSectionTeacher = Vue.component('admin-section-teacher',{
    template:`
        <div id="adminSectionTeacher">
            <div id="manageTeachers" class="adminManageSection">
                <button v-on:click="manageTeachersButton" class="manageButton">
                    <img src="icons/teacher.png" alt="Gestisci docenti"></button>
                <div v-if="manageTeachers" class="insertTeacher">
                    <button class="adminFunctionsButton" v-if="!viewTeachersInsert" v-on:click="toggleTeachersInput">Inserisci docente</button>
                    <button class="adminFunctionsButton" v-if="viewTeachersInsert" v-on:click="toggleTeachersInput">Annulla</button>
                    <br>
                    <input v-if="viewTeachersInsert" v-model="selectedTeacherName" placeholder="Nome Docente"/>
                    <input v-if="viewTeachersInsert" v-model="selectedTeacherSurname" placeholder="Cognome Docente"/>
                    <button class="adminFunctionsButton" v-if="viewTeachersInsert" v-on:click="insertTeacher">Inserisci</button>
                    <div v-if="result && viewTeachersInsert"><img src="icons/icon-done.svg"></div>
                    <div v-if="result === false && viewTeachersInsert"><img src="icons/icon-warning.png"></div>
                </div>

                <div v-if="manageTeachers" class="removeTeacher">
                    <button class="adminFunctionsButton" v-if="!viewTeachersRemove" v-on:click="toggleTeachers">Elimina un docente</button>
                    <button class="adminFunctionsButton" v-if="viewTeachersRemove" v-on:click="toggleTeachers">Annulla</button>
                    <select v-if="viewTeachersRemove" id="removeTeacherField" v-model="selectedTeacher">
                        <option disabled value=""></option>
                        <option v-for="teacher in teachersList">{{teacher.Surname}}, {{teacher.Name}}</option>
                    </select>
                    <button class="adminFunctionsButton" v-if="viewTeachersRemove" v-on:click="removeTeacher">Elimina</button>
                    <div v-if="result && viewTeachersRemove"><img src="icons/icon-done.svg"></div>
                    <div v-if="result === false && viewTeachersRemove"><img src="icons/icon-warning.png"></div>
                </div>
            </div>
        </div>
    `,
    data: function() {
        return {
            manageTeachers: false,
            viewTeachersInsert: false,
            viewTeachersRemove: false,
            teachersList: [],
            selectedTeacher: null,
            selectedTeacherName: null,
            selectedTeacherSurname: null,
            result: null,
            link: 'ServletAdmin'
        }
    },
    methods: {
        manageTeachersButton: function () {
            this.manageTeachers = !this.manageTeachers;
        },
        toggleTeachersInput: function () {
            this.result = null;
            this.viewTeachersInsert = (!this.viewTeachersInsert);
        },
        toggleTeachers: function () {
            if(!this.viewTeachersRemove) {
                this.result = null;
                this.viewTeachersRemove = true;
                this.getTeachersToDelete();
            } else {
                this.viewTeachersRemove = false;
            }
        },
        insertTeacher: function () {
            var self = this;
            $.post(this.link, {Action: 'InsertTeacher', TeacherName: self.selectedTeacherName,
                TeacherSurname: self.selectedTeacherSurname}, function(data) {
                self.result = data;
            });
        },
        removeTeacher: function () {
            var self = this;
            $.post(this.link, {Action: 'RemoveTeacher', Teacher: self.selectedTeacher}, function(data) {
                self.result = data;
            });
        },
        getTeachersToDelete: function () {
            var self = this;
            $.get(this.link, {Action: 'GetTeachersToDelete'}, function(data) {
                self.teachersList = data;
            });
        }
    }
});


var adminSectionCourse = Vue.component('admin-section-course',{
    template:`        
        <div id="adminSectionCourse">
            <div id="manageCourses" class="adminManageSection">
                <button v-on:click="manageCoursesButton" class="manageButton">
                    <img src="icons/online-course.png" alt="Gestisci corsi"></button>
                <div v-if="manageCourses" class="insertCourse">
                    <button class="adminFunctionsButton" v-if="!viewCoursesInsert" v-on:click="toggleCoursesInput">Inserisci corso</button>
                    <button class = "adminFunctionsButton" v-if="viewCoursesInsert" v-on:click="toggleCoursesInput">Annulla</button>
                    <input v-if="viewCoursesInsert" v-model="selectedCourse" placeholder="Nome del Corso"/>
                    <button class="adminFunctionsButton" v-if="viewCoursesInsert" v-on:click="insertCourse">Inserisci</button>
                    <div v-if="result && viewCoursesInsert"><img src="icons/icon-done.svg"></div>
                    <div v-if="result === false && viewCoursesInsert"><img src="icon-warning.png"></div>
                </div>

                <div v-if="manageCourses" class="removeCourse">
                    <button class="adminFunctionsButton" v-if="!viewCoursesRemove" v-on:click="toggleCourses">Elimina un corso</button>
                    <button class="adminFunctionsButton" v-if="viewCoursesRemove" v-on:click="toggleCourses">Annulla</button>
                    <select v-if="viewCoursesRemove" id="removeCourseField" v-model="selectedCourse">
                        <option disabled value=""></option>
                        <option v-for="course in coursesList">{{course.Title}}</option>
                    </select>
                    <button class="adminFunctionsButton" v-if="viewCoursesRemove" v-on:click="removeCourse">Elimina</button>
                    <div v-if="result && viewCoursesRemove"><img src="icons/icon-done.svg"></div>
                    <div v-if="result === false && viewCoursesRemove"><img src="icons/icon-warning.png"></div>
                </div>
            </div>
        </div>
    `,
    data: function() {
        return {
            manageCourses: false,
            viewCoursesInsert: false,
            viewCoursesRemove: false,
            coursesList: [],
            selectedCourse: null,
            result: null,
            link: 'ServletAdmin'
        }
    },
    methods: {
        manageCoursesButton: function () {
            this.manageCourses = !this.manageCourses;
        },
        toggleCoursesInput: function () {
            this.result = null;
            this.viewCoursesInsert = (!this.viewCoursesInsert);
        },
        toggleCourses: function () {
            if (!this.viewCoursesRemove) {
                this.result = null;
                this.viewCoursesRemove = true;
                this.getCoursesToDelete();
            } else {
                this.viewCoursesRemove = false;
            }
        },
        insertCourse: function () {
            var self = this;
            $.post(this.link, {Action: 'InsertCourse', Course: self.selectedCourse}, function (data) {
                self.result = data;
            });
        },
        removeCourse: function () {
            var self = this;
            $.post(this.link, {Action: 'RemoveCourse', Course: self.selectedCourse}, function(data) {
                self.result = data;
            });
        },
        getCoursesToDelete: function () {
            var self = this;
            $.get(this.link, {Action: 'GetCoursesToDelete'}, function(data) {
                self.coursesList = data;
            });
        }
    }
});

var adminSectionTeaching = Vue.component('admin-section-teaching', {
    template:`
        <div id="adminSectionTeaching">
            <div id="manageTeachings" class="adminManageSection">
                <button v-on:click="manageTeachingsButton" class="manageButton">
                    <img src="icons/teachings.png" alt="Gestisci associazioni"></button>
                <div v-if="manageTeachings" class="insertTeaching">
                    <button class="adminFunctionsButton" v-if="!viewTeachingInsert" v-on:click="toggleInsertTeaching">Inserisci l'associazione</button>
                    <button class="adminFunctionsButton" v-if="viewTeachingInsert" v-on:click="toggleInsertTeaching">Annulla</button>
                    <select v-if="viewTeachingInsert" id="insertTeacherTeachingField" v-model="selectedTeacher">
                        <option disabled value=""></option>
                        <option v-for="teacher in teachersList">{{teacher.Surname}}, {{teacher.Name}}</option>
                    </select>
                    <select v-if="viewTeachingInsert" id="insertCourseTeachingField" v-model="selectedCourse">
                        <option disabled value=""></option>
                        <option v-for="course in coursesList">{{course.Title}}</option>
                    </select>
                    <button class="adminFunctionsButton" v-if="viewTeachingInsert === true" v-on:click="insertTeaching">Inserisci</button>
                    <div v-if="result && viewTeachingInsert"><img src="icons/icon-done.svg"></div>
                    <div v-if="result === false && viewTeachingInsert"><img src="icons/icon-warning.png"></div>
                </div>
                <div v-if="manageTeachings" class="removeTeaching">
                    <button class="adminFunctionsButton" v-if="!viewTeachingRemove" v-on:click="toggleTeachings">Elimina un'associazione corso/docente</button>
                    <button class="adminFunctionsButton" v-if="viewTeachingRemove" v-on:click="toggleTeachings">Annulla</button>
                    <select v-if="viewTeachingRemove" id="removeTeachingField" v-model="selectedTeaching">
                        <option disabled value=""></option>
                        <option v-for="teaching in teachingList">{{teaching.teacher}}-{{teaching.course}}</option>
                    </select>
                    <button class="adminFunctionsButton" v-if="viewTeachingRemove" v-on:click="removeTeaching">Elimina</button>
                    <div v-if="result && viewTeachingRemove">
                        <img src="icons/icon-done.svg">
                    </div>
                    <div v-if="result === false && viewTeachingRemove">
                        <img src="icons/icon-warning.png">
                    </div>
                </div>
            </div>
        </div>
    `,
    data: function() {
        return {
            manageTeachings: false,
            viewTeachingInsert: false,
            viewTeachingRemove: false,
            coursesList: [],
            teachersList: [],
            teachingList: [],
            selectedCourse: null,
            selectedTeacher: null,
            result: null,
            link: 'ServletAdmin'
        }
    },
    methods: {
        manageTeachingsButton: function () {
            this.manageTeachings = !this.manageTeachings;
        },
        toggleInsertTeaching: function () {
            if(!this.viewTeachingInsert) {
                this.result = null;
                this.viewTeachingInsert = true;
                this.getCoursesToDelete();
                this.getTeachersToDelete();
            } else {
                this.viewTeachingInsert = false;
            }
        },
        toggleTeachings: function () {
            if (!this.viewTeachingRemove) {
                this.result = null;
                this.viewTeachingRemove = true;
                var self = this;
                $.get(this.link, {Action: 'GetTeachingsToDelete'}, function(data) {
                    self.teachingList = data;
                });
            } else {
                this.viewTeachingRemove = false;
            }
        },
        insertTeaching: function () {
            var self = this;
            $.post(this.link, {Action: 'InsertTeaching', Teacher: self.selectedTeacher, Course:self.selectedCourse},
                function (data) {
                    self.result = data;
                });
        },
        removeTeaching: function () {
            var self = this;
            $.post(this.link, {Action: 'RemoveTeaching', Teaching: self.selectedTeaching}, function(data) {
                self.result = data;
            });
        },
        getCoursesToDelete: function () {
            var self = this;
            $.get(this.link, {Action: 'GetCoursesToDelete'}, function(data) {
                self.coursesList = data;
            });
        },
        getTeachersToDelete: function () {
            var self = this;
            $.get(this.link, {Action: 'GetTeachersToDelete'}, function(data) {
                self.teachersList = data;
            });
        }
    }
});

var cta = Vue.component('join-us',{
    template:`
           <section class="cta">
                <h2>Entra nel team!</h2>
                <p>Diventa anche tu un docente di <span>Ripetiamo</span>.</p>
                <a href="mailto:ripetiamora@gmail.com" class="hero-btn">Contattaci</a>
            </section>
    `
});

var footer = Vue.component('footer-section', {
    template:`
        <footer>
                <div class="footer-row" id="credits" v-if="this.Role==='ospite'">
                    <ul>
                        <li v-if="this.Role === 'ospite'"><a href="signup.html">Iscriviti <span>ora</span></a></li>
                        <li ><a href="#viewAvailableLessons">Le <span>nostre</span> lezioni</a></li>
                        <li><a href="mailto:ripetiamora@gmail.com?subject=Vorrei%20diventare%20un%20professore%20di%20Ripetiamo">Unisciti a <span>noi</span></a></li>
                        <li><a href="mailto:ripetiamora@gmail.com?subject=Ho%20Bisogno%20di%20aiuto">Hai bisogno di <span>aiuto</span>?</a></li>
                    </ul>
                </div>

                <hr>

                <div class="footer-row">
                    <div class="social-icons">
                        <a href="https://www.instagram.com/ripetiamora/">
                            <img src="icons/icons8-instagram-48.svg" alt="Instagram profile">
                        </a>
                        <a href="https://www.facebook.com/">
                            <img src="icons/icons8-facebook-48.svg" alt="Facebook profile">
                        </a>
                        <a href="https://www.twitter.com/">
                            <img src="icons/icons8-twitter-48.svg" alt="Twitter profile">
                        </a>
                        <a href="https://www.tiktok.com/">
                            <img src="icons/icons8-tiktok-48.svg" alt="TikTok profile">
                        </a>
                    </div>
                </div>

                <hr>

                <div class="footer-row">
                    <p>&copy; 2022, <span>Ripetiamo</span></p>
                </div>
            </footer>
    `,
    props: ['Role']
});


var loginField = Vue.component('loginfield',{
    template: `
        <div class="loginPage">
             <div class="loginContainer">
              <img src="icons/writing1.png" class="loginTitle">
              <div class="loginForm">
                 <div id="loginFieldsUsername" class="loginFields">
                      <input v-model="loginUsername" type="text" placeholder="Username" required/>
                      <span></span>
                      <label>Username</label>
                    </div>
                    <div id="loginFieldsPassword" class="loginFields">
                      <input v-model="loginPassword" type="password" placeholder="Password" required/>
                      <span></span>
                      <label>Password</label>
                    </div>
                    <button v-on:click="loginInner" class="loginButton">Accedi</button>
                  <div class="register">
                    Non hai un account? <a href="signup.html">Registrati subito</a>
                  </div>
                </div>
             </div>
        </div>
    `,
    data: function() {
        return {
            ruolo: '',
            loginUsername: '',
            loginPassword: '',
            sessione: 'sessione inesistente',
            nome: '',
            link: 'ServletAutentica'
        }
    },
    methods:{
        loginInner: function () {
            this.$emit('login-inner', this.loginUsername, this.loginPassword);
        }
    }
});


var app = new Vue({
    data: {
        login: false,
        Role: 'ospite',
        Username: null,
        result: null,
        linkServletUser: 'ServletUser',
        linkLogin: 'ServletAutentica',
        linkLogout: 'ServletLogout',
        clientView: 'BaL'
    },
    el: '#app',
    mounted() {
        this.getUserData();
    },
    methods: {
        getUserData: function () {
            var self = this;
            $.get(this.linkServletUser, {}, function (data) {
                if(data !== null) {
                    self.Username = data.Username;
                    self.Role = data.Role;
                }
            });
        },
        switchLoginView: function () {
            this.login = true;
        },
        loginMethod: function(username, password){
            var self = this;
            $.post(this.linkLogin, {loginUsername:username, loginPassword: password}, function (data) {
                if(data !== false) {
                    self.Username = data.Username;
                    self.Role = data.Role;
                } else {
                    alert("I dati inseriti non sono corretti!")
                }
                if(self.Username != null && self.Role !== 'ospite') {
                    self.login = false;
                }
            });
        },
        logoutMethod: function() {
            var self = this;
            $.post(this.linkLogout, {}, function () {
                self.Username = null;
                self.Role = 'ospite';
                window.location.reload();
            });
        },
        switchClientView: function(section) {
            this.clientView = section;
        },
        sendEmail: function() {
            window.open('mailto:ripetiamora@gmail.com');
        }
    }
});
