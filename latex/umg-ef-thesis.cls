%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% File:    umg-ef-thesis.cls  (LaTeX Document class option "umg-ef-thesis")
%
% Author:  Krzysztof Michał Klejbor    (krzysztof.klejbor@gmail.com)
%          Gdynia Maritime University, Electrical Faculty, Gdynia, POLAND
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

\message{*** [umg-ef-thesis.cls] --- (KMK) v1.0 <14.05.2024> ***}

\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{umg-ef-thesis}
\def\@baseclass{report}
\DeclareOption*{\PassOptionsToClass{\CurrentOption}{\@baseclass}}
\PassOptionsToClass{a4paper,twoside,openright,12pt}{\@baseclass}
\ProcessOptions

\LoadClass{\@baseclass}

\RequirePackage[utf8]{inputenc}
\RequirePackage{polski}
\RequirePackage{geometry}
\RequirePackage{times}
\RequirePackage{mathptmx}
\RequirePackage{graphicx}
\RequirePackage{multirow}
\RequirePackage{xcolor}
\RequirePackage[T1]{fontenc}
\RequirePackage{tabularx}
\RequirePackage{ulem}

\geometry{lmargin=35mm, rmargin=25mm, tmargin=25mm, bmargin=25mm, foot=10mm, head=10mm}

\frenchspacing

\linespread{1.5}
\setlength\parindent{1cm}

\renewcommand{\author}[1][imię/imiona nazwisko] {#1}
\renewcommand{\title}[1][tytuł pracy] {#1}
\def\supervisor[#1][imię nazwisko wraz z tytułami]   {\gdef\@supervisor[#1]}
\def\reviewer[#1][imię nazwisko wraz z tytułami]   {\gdef\@reviewer[#1]}
\def\studentsBook[#1][numer]   {\gdef\@studentsBook[#1]}
\def\acknowledgementDate[#1][data oświadczenia]   {\gdef\@acknowledgementDate[#1]}
\def\examDate[#1][dd.mm.rr]   {\gdef\@examDate[#1]}
\def\specialty[#1][specjalność]   {\gdef\@specialty[#1]}
\def\superviorsGrade[#1][ocena]   {\gdef\@superviorsGrade[#1]}
\def\reviewersGrade[#1][ocena]   {\gdef\@reviewersGrade[#1]}
\def\result[#1][wynik]   {\gdef\@result[#1]}


\newcolumntype{C}{>{\centering\arraybackslash}X}

\renewcommand{\maketitle}{%
    \thispagestyle{empty}
    \noindent
    \begin{minipage}{.6\linewidth}
        \begin{flushleft}
            \footnotesize
            \textbf{UNIWERSYTET MORSKI W GDYNI} \\ 
            \textbf{WYDZIAŁ ELEKTRYCZNY} \\
            Katedra [ Nazwa Katedry ]
        \end{flushleft}
    \end{minipage}
    \begin{minipage}{.4\linewidth}
        \begin{flushright}
            \scriptsize
        Nr ewidencyjny:\dotfill \\
        Data złożenia pracy:\dotfill \\
        \hfill
        \end{flushright}
    \end{minipage}

    \begin{figure}[htbp]
        \centerline{\includegraphics[width=3.64cm,height=3.75cm]{umg-zolty.png}}
    \end{figure}
    \centerline{\fontsize{16pt}{\baselineskip} \selectfont\textbf{ PRACA DYPLOMOWA INŻYNIERSKA}}
    \vspace{1cm}

    \noindent 
    Temat: [ Tytuł pracy ] 
    \vspace{1.5cm}

    \begin{center}
        \footnotesize
        \begin{tabularx}{\textwidth}{|l|C|l|}
            \hline
            Dyplomant: & [ Imię/imiona Nazwisko ] & Numer albumu: [ numer ] \\
            \hline
            Specjalność: & \multicolumn{2}{|c|}{[ specjalność ]}\\
            \hline
            Promotor: & [ Imię Nazwisko wraz z tytułami ] & Ocena: [ ocena ]\\
            \hline
            Recenzent & [ Imię Nazwisko wraz z tytułami ] & Ocena: [ ocena ]\\
            \hline
            Data egzaminu: & [ dd.mm.rrrr ] & Wynik studiów: [ wynik ] \\
            \hline
        \end{tabularx}
    \end{center}


    \vspace{1.5cm}
    \noindent
    \footnotesize
    Recenzent:\dotfill Promotor:\dotfill Dziekan:\dotfill

    \vfill
    \begin{flushright}
        \begin{minipage}{.5\linewidth}
            \begin{center}
                \scriptsize
                Wyrażam zgodę / nie wyrażam zgody na\\
                udostępnianie mojej pracy dyplomowej
                
                \vspace{1cm}
                \begin{minipage}{.37\linewidth}
                    \begin{center}
                        \dotfill \\
                        data
                    \end{center}
                \end{minipage}
                \begin{minipage}{.47\linewidth}
                    \begin{center}
                        \vspace{0.1cm}
                        \dotfill \\
                        podpis
                    \end{center}
                \end{minipage}
            \end{center}
        \end{minipage}
    \end{flushright}

    \newpage

    \thispagestyle{empty}

    \vspace*{1cm}
    \noindent
    \begin{minipage}{0.5\linewidth}
        \begin{flushleft}
            \begin{minipage}{.8\linewidth}
                \begin{center}
                    \dotfill \\
                    Imię i Nazwisko
                \end{center}
            \end{minipage}
        \end{flushleft}
    \end{minipage}
    \begin{minipage}{0.5\linewidth}
        \begin{flushright}
                Gdynia, dnia [data oświadczenia] r.\\
                \hfill
        \end{flushright}
    \end{minipage}

    \vspace{3cm}
    \centerline{\large \textbf{OŚWIADCZENIE}}\vspace{1cm}

    Świadomy/a odpowiedzialności prawnej oświadczam, że złożona praca inżynierska/magisterska* pt.:\textit{[ tytuł pracy ]} została napisana przeze mnie samodzielnie. 

    Równocześnie oświadczam, że w pracy wykorzystano tylko cytowaną literaturę a więc praca nie narusza praw autorskich w rozumieniu ustawy z dnia 4 lutego 1994 roku o prawie autorskim i~prawach pokrewnych (Dz. U. 1994, nr 24, poz. 83) oraz dóbr osobistych chronionych prawem cywilnym. 

    Ponadto praca nie zawiera informacji i danych uzyskanych w sposób nielegalny i~nie była wcześniej przedmiotem innych procedur urzędowych związanych z uzyskaniem dyplomów lub tytułów zawodowych uczelni wyższej. 

    Oświadczam ponadto, że niniejsza wersja pracy jest identyczna z załączoną wersją elektroniczną na płycie CD.

    \color{red} 
    Na podstawie art. 75 §2 kodeksu postępowania administracyjnego wnoszę o odebranie tego oświadczenia jako dowodu prawdziwości okoliczności w nim podatnych, przy czym jestem świadomy odpowiedzialności karnej z art. 233 §1 i §6 k.k. za złożenie fałszywego oświadczenia. 
    \color{black}

    \vspace{1.5cm}
    \begin{flushright}
        \begin{minipage}{.5\linewidth}
            \begin{center}
                \dotfill \\
                podpis
            \end{center}
        \end{minipage}
    \end{flushright}

    \vfill
    \begin{flushleft}
    * niepotrzebne skreślić
    \end{flushleft}

    \newpage
}