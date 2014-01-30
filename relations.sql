DROP TABLE "user";
DROP TABLE "group";
DROP TABLE "user_group";
DROP TABLE "post";
DROP TABLE "file";

CREATE TABLE "user" (
user_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
user_name VARCHAR(50),
user_password VARCHAR(64),
user_moderator BOOLEAN DEFAULT FALSE,
user_email VARCHAR(50),
user_tmp_code VARCHAR(50),
user_tmp_code_time TIMESTAMP,
user_last_time TIMESTAMP,
PRIMARY KEY (user_id),
UNIQUE (user_name, user_email)
);

CREATE TABLE "group" (
group_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
group_name VARCHAR(50),
creator_id INTEGER,
group_public BOOLEAN DEFAULT TRUE,
group_closed BOOLEAN DEFAULT FALSE,
PRIMARY KEY (group_id),
UNIQUE (group_name)
);

CREATE TABLE "post" (
post_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
user_id INTEGER,
group_id INTEGER,
post_text VARCHAR(32672),
post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (post_id)
);
CREATE INDEX post_date_index ON "post"(post_date);

CREATE TABLE "user_group" (
user_id INTEGER,
group_id INTEGER,
group_accepted BOOLEAN DEFAULT FALSE,
visible BOOLEAN DEFAULT TRUE,
PRIMARY KEY (user_id, group_id)
);

CREATE TABLE "file" (
post_id INTEGER,
file_name VARCHAR(50),
file_mime VARCHAR(50),
file_size INTEGER DEFAULT 0,
PRIMARY KEY (post_id, file_name)
);

CREATE INDEX post_date_index ON STUDENTS_FORUM."post"(post_date);

INSERT INTO "user"(user_name, user_password, user_email, user_moderator) VALUES('user1', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'paolomaffini@gmail.com', TRUE);
INSERT INTO "user"(user_name, user_password, user_email, user_moderator) VALUES('user2', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'maffonline@live.com', TRUE);
INSERT INTO "user"(user_name, user_password, user_email) VALUES('user3', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'paolomaffini@yahoo.it');
INSERT INTO "user"(user_name, user_password, user_email) VALUES('user4', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'halfblood17@gmail.com');
INSERT INTO "user"(user_name, user_password, user_email) VALUES('user5', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'spazioanime@gmail.com');
INSERT INTO "user"(user_name, user_password, user_email) VALUES('user6', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'freip90@gmail.com');
INSERT INTO "user"(user_name, user_password, user_email) VALUES('user7', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'freip@hotmail.it');
INSERT INTO "user"(user_name, user_password, user_email) VALUES('user8', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'trovacontest@gmail.com');
INSERT INTO "user"(user_name, user_password, user_email) VALUES('user9', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'simone.giuditta@gmail.com');
INSERT INTO "user"(user_name, user_password, user_email) VALUES('user10', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'maffonline@icloud.com');

/* GROUPS */
INSERT INTO "group"(group_name, creator_id) VALUES('Analisi', 3);
INSERT INTO "group"(group_name, creator_id) VALUES('Algoritmi', 4);
INSERT INTO "group"(group_name, creator_id) VALUES('Statistica', 5);
INSERT INTO "group"(group_name, creator_id) VALUES('Reti', 6);
INSERT INTO "group"(group_name, creator_id, group_closed) VALUES('Web', 7, TRUE);

/* GROUP MEMBERS */
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(1, 1, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(1, 2, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(1, 3, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(1, 4, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(1, 5, TRUE);

INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(2, 2, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(2, 3, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(2, 4, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(2, 5, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(2, 6, TRUE);

INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(3, 3, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(3, 4, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(3, 5, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(3, 6, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(3, 7, TRUE);

INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(4, 4, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(4, 5, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(4, 6, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(4, 7, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(4, 8, TRUE);

INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(5, 6, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(5, 7, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(5, 8, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(5, 9, TRUE);
INSERT INTO "user_group"(group_id, user_id, group_accepted) VALUES(5, 10, TRUE);

/* GROUP POSTS */
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 1, 'Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 2, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 3, 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 4, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 5, 'Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. ');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 1, 'Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 2, 'Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 3, 'Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 4, 'Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(1, 5, 'At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. ');

INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 2, 'Et harum quidem rerum facilis est et expedita distinctio.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 3, 'Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 4, 'Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 5, 'Gallia est omnis divisa in partes tres, quarum unam incolunt Belgae, aliam Aquitani, tertiam qui ipsorum lingua Celtae, nostra Galli appellantur.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 6, 'Hi omnes lingua, institutis, legibus inter se differunt. Gallos ab Aquitanis Garunna flumen, a Belgis Matrona et Sequana dividit.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 2, 'Horum omnium fortissimi sunt Belgae, propterea quod a cultu atque humanitate provinciae longissime absunt minimeque ad eos mercatores saepe commeant atque ea, quae ad effeminandos animos pertinent, important proximique sunt Germanis, qui trans Rhenum incolunt, quibuscum continenter bellum gerunt.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 3, 'Qua de causa Helvetii quoque reliquos Gallos virtute praecedunt, quod fere cotidianis proeliis cum Germanis contendunt, cum aut suis finibus eos prohibent aut ipsi in eorum finibus bellum gerunt.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 4, 'Eorum una pars, quam Gallos obtinere dictum est, initium capit a flumine Rhodano, continetur Garunna flumine, Oceano, finibus Belgarum, attingit etiam ab Sequanis et Helvetiis flumen Rhenum, vergit ad septentriones. ');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 5, 'Belgae ab extremis Galliae finibus oriuntur, pertinent ad inferiorem partem fluminis Rheni, spectant in septentrionem et orientem solem.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(2, 6, 'Aquitania a Garunna flumine ad Pyrenaeos montes et eam partem Oceani, quae est ad Hispaniam, pertinet, spectat inter occasum solis et septentriones.');

INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 3, 'Apud Helvetios longe nobilissimus fuit et ditissimus Orgetorix. is M. Messala [et P.] M. Pisone consulibus regni, cupiditate inductus coniurationem nobilitatis fecit et civitati persuasit, ut de finibus suis cum omnibus copiis exirent: perfacile esse, cum virtute omnibus praestarent, totius Galliae imperio potiri.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 4, 'Id hoc facilius iis persuasit, quod undique loci natura Helvetii continentur: una ex parte flumine Rheno latissimo atque altissimo, qui agrum Helvetium a Germanis dividit, altera ex parte monte Iura altissimo, qui est inter Sequanos et Helvetios, tertia lacu Lemanno et flumine Rhodano, qui provinciam nostram ab Helvetiis dividit.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 5, 'His rebus fiebat ut et minus late vagarentur et minus facile finitimis bellum inferre possent; qua ex parte homines bellandi cupidi magno dolore adficiebantur.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 6, 'Pro multitudine autem hominum et pro gloria belli atque fortitudinis angustos se fines habere arbitrabantur, qui in longitudinem milia passuum ccxl, in latitudinem clxxx patebant.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 7, 'Ea res est Helvetiis per indicium enuntiata. ');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 3, 'Moribus suis Orgetorigem ex vinculis causam dicere coegerunt; damnatum poenam sequi oportebat ut igni cremaretur.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 4, 'Die constituta causae dictionis Orgetorix ad iudicium omnem suam familiam, ad hominum milia decem, undique coegit et omnes clientes obaeratosque suos, quorum magnum numerum habebat, eodem conduxit; per eos ne causam diceret se eripuit.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 5, 'Cum civitas ob eam rem incitata armis ius suum exsequi conaretur multitudinemque hominum ex agris magistratus cogerent, Orgetorix mortuus est; neque abest suspicio, ut Helvetii arbitrantur, quin ipse sibi mortem consciverit.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 6, 'Interea ea legione, quam secum habebat, militibusque, qui ex provincia convenerant, a lacu Lemanno, qui in flumen Rhodanum influit, ad montem Iuram, qui fines Sequanorum ab Helvetiis dividit, milia passuum decem novem murum in altitudinem pedum sedecim fossamque perducit. ');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(3, 7, 'Eo opere perfecto praesidia disponit, castella communit, quo facilius si se invito transire conarentur, prohibere possit.');

INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 4, 'Ubi ea dies quam constituerat cum legatis, venit et legati ad eum reverterunt, negat se more et exemplo populi Romani posse iter ulli per provinciam dare et, si vim facere conentur, prohibiturum ostendit');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 5, 'Helvetii ea spe deiecti navibus iunctis ratibusque compluribus factis, alii vadis Rhodani, qua minima altitudo fluminis erat, nonnumquam interdiu, saepius noctu si perrumpere possent conati, operis munitione et militum concursu et telis repulsi hoc conatu destiterunt');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 6, 'Relinquebatur una per Sequanos via, qua Sequanis invitis propter angustias ire non poterant.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 7, 'His cum sua sponte persuadere non possent, legatos ad Dumnorigem Haeduum mittunt, ut eo deprecatore a Sequanis impetrarent.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 8, 'Dumnorix gratia et largitione apud Sequanos plurimum poterat et Helvetiis erat amicus, quod ex ea civitate Orgetorigis filiam in matrimonium duxerat, et cupiditate regni adductus novis rebus studebat et quam plurimas civitates suo beneficio habere obstrictas volebat.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 4, 'Itaque rem suscipit et a Sequanis impetrat, ut per fines suos Helvetios ire patiantur, obsidesque uti inter sese dent perficit: Sequani, ne itinere Helvetios prohibeant, Helvetii, ut sine maleficio et iniuria transeant.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 5, 'Caesari nuntiatur Helvetiis esse in animo per agrum Sequanorum et Haeduorum iter in Santonum fines facere, qui non longe a Tolosatium finibus absunt, quae civitas est in provincia.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 6, 'Id si fieret, intellegebat magno cum periculo provinciae futurum, ut homines bellicosos, populi Romani inimicos, locis patentibus maximeque frumentariis finitimos haberet.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 7, 'Ob eas causas ei munitioni, quam fecerat, T. Labienum legatum praefecit; ipse in Italiam magnis itineribus contendit duasque ibi legiones conscribit et tres, quae circum Aquileiam hiemabant, ex hibernis educit et, qua proximum iter in ulteriorem Galliam per Alpes erat, cum his quinque legionibus ire contendit.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(4, 8, 'Ibi Ceutrones et Graioceli et Caturiges locis superioribus occupatis itinere exercitum prohibere conantur.');

INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 6, 'Compluribus his proeliis pulsis ab Ocelo, quod est citerioris provinciae extremum, in fines Vocontiorum ulterioris provinciae die septimo pervenit; inde in Allobrogum fines, ab Allobrogibus in Segusiavos exercitum ducit.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 7, 'Hi sunt extra provinciam trans Rhodanum primi');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 8, 'Helvetii iam per angustias et fines Sequanorum suas copias traduxerant et in Haeduorum fines pervenerant eorumque agros populabantur.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 9, 'Haedui cum se suaque ab iis defendere non possent, legatos ad Caesarem mittunt rogatum auxilium: ita se omni tempore de populo Romano meritos esse, ut paene in conspectu exercitus nostri agri vastari, liberi eorum in servitutem abduci, oppida expugnari non debuerint. ');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 10, 'Eodem tempore Ambarri, necessarii et consanguinei Haeduorum, Caesarem certiorem faciunt sese depopulatis agris non facile ab oppidis vim hostium prohibere.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 9, 'Item Allobroges qui trans Rhodanum vicos possessionesque habebant, fuga se ad Caesarem recipiunt et demonstrant sibi praeter agri solum nihil esse reliqui.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 10, 'Quibus rebus adductus Caesar non exspectandum sibi statuit, dum omnibus fortunis sociorum consumptis in Santonos Helvetii pervenirent.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 8, 'Flumen est Arar, quod per fines Haeduorum et Sequanorum in Rhodanum influit, incredibili lenitate, ita ut oculis in utram partem fluat iudicari non possit.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 7, 'Id Helvetii ratibus ac lintribus iunctis transibant.');
INSERT INTO "post"(group_id, user_id, post_text) VALUES(5, 6, 'Ubi per exploratores Caesar certior factus est tres iam partes copiarum Helvetios id lumen traduxisse, quartam vero partem citra flumen Ararim reliquam esse, de tertia vigilia cum legionibus tribus e castris profectus ad eam partem pervenit quae nondum flumen transierat.');

/* GROUP FILES */
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(1, 'img1', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(2, 'img2', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(3, 'img3', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(4, 'img4', 'image/jpeg');

INSERT INTO "file"(post_id, file_name, file_mime) VALUES(11, 'img1', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(12, 'img2', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(13, 'img3', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(14, 'img4', 'image/jpeg');

INSERT INTO "file"(post_id, file_name, file_mime) VALUES(21, 'img1', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(22, 'img2', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(23, 'img3', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(24, 'img4', 'image/jpeg');

INSERT INTO "file"(post_id, file_name, file_mime) VALUES(31, 'img1', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(32, 'img2', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(33, 'img3', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(34, 'img4', 'image/jpeg');

INSERT INTO "file"(post_id, file_name, file_mime) VALUES(41, 'img1', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(42, 'img2', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(43, 'img3', 'image/jpeg');
INSERT INTO "file"(post_id, file_name, file_mime) VALUES(44, 'img4', 'image/jpeg');