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
\LoadClass[a4paper,twoside,openright,12pt]{report}
% \def\@baseclass{report}
% \DeclareOption*{\PassOptionsToClass{\CurrentOption}{\@baseclass}}
% \PassOptionsToClass{}{\@baseclass}

\newcommand{\@myAuthor}{imie/imiona nazwisko}
\newcommand{\@myTitle}{tytuł pracy}
\newcommand{\@supervisor}{imię nazwisko wraz z tytułami}
\newcommand{\@reviewer}{imię nazwisko wraz z tytułami}
\newcommand{\@studentsBook}{numer}
\newcommand{\@acknowledgementDate}{data oświadczenia}
\newcommand{\@examDate}{dd.mm.rr}
\newcommand{\@specialty}{specjalność}
\newcommand{\@superviorsGrade}{ocena}
\newcommand{\@reviewersGrade}{ocena}
\newcommand{\@result}{wynik}
\newcommand{\@professorship}{nazwa katedry}

\DeclareOption{title}{\renewcommand{\@myTitle}{#1}}
\DeclareOption{author}{\renewcommand{\@myAuthor}{#1}}
\DeclareOption{supervisor}{\renewcommand{\@supervisor}{#1}}
\DeclareOption{reviewer}{\renewcommand{\@reviewer}{#1}}
\DeclareOption{studentsBook}{\renewcommand{\@studentsBook}{#1}}
\DeclareOption{acknowledgementDate}{\renewcommand{\@acknowledgementDate}{#1}}
\DeclareOption{examDate}{\renewcommand{\@examDate}{#1}}
\DeclareOption{specialty}{\renewcommand{\@specialty}{#1}}
\DeclareOption{superviorsGrade}{\renewcommand{\@superviorsGrade}{#1}}
\DeclareOption{reviewersGrade}{\renewcommand{\@reviewersGrade}{#1}}
\DeclareOption{result}{\renewcommand{\@result}{#1}}
\DeclareOption{professorship}{\renewcommand{\@professorship}{#1}}

% Default options
\ExecuteOptions{title={},author={},supervisor={},reviewer={},studentsBook={},acknowledgementDate={},examDate={},specialty={},superviorsGrade={},result={},professorship={}}

\ProcessOptions\relax

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
\RequirePackage{soul}

\geometry{lmargin=35mm, rmargin=25mm, tmargin=25mm, bmargin=25mm, foot=10mm, head=10mm}

\frenchspacing

\linespread{1.5}
\setlength\parindent{1cm}

\newcommand{\myAuthor}[1]{\renewcommand{\@myAuthor}{#1}}
\newcommand{\myTitle}[1]{\renewcommand{\@myTitle}{#1}}
\newcommand{\supervisor}[1]{\renewcommand{\@supervisor}{#1}}
\newcommand{\reviewer}[1]{\renewcommand{\@reviewer}{#1}}
\newcommand{\studentsBook}[1]{\renewcommand{\@studentsBook}{#1}}
\newcommand{\acknowledgementDate}[1]{\renewcommand{\@acknowledgementDate}{#1}}
\newcommand{\examDate}[1]{\renewcommand{\@examDate}{#1}}
\newcommand{\specialty}[1]{\renewcommand{\@specialty}{#1}}
\newcommand{\superviorsGrade}[1]{\renewcommand{\@superviorsGrade}{#1}}
\newcommand{\reviewersGrade}[1]{\renewcommand{\@reviewersGrade}{#1}}
\newcommand{\result}[1]{\renewcommand{\@result}{#1}}
\newcommand{\professorship}[1]{\renewcommand{\@professorship}{#1}}

\newcolumntype{C}{>{\centering\arraybackslash}X}

\renewcommand{\maketitle}{%
    \thispagestyle{empty}
    \noindent
    \begin{minipage}{.6\linewidth}
        \begin{flushleft}
            \footnotesize
            \textbf{UNIWERSYTET MORSKI W GDYNI} \\ 
            \textbf{WYDZIAŁ ELEKTRYCZNY} \\
            Katedra \@professorship
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
    Temat: \@myTitle
    \vspace{1.5cm}

    \begin{center}
        \footnotesize
        \begin{tabularx}{\textwidth}{|l|C|l|}
            \hline
            Dyplomant: & \@myAuthor & Numer albumu: \@studentsBook \\
            \hline
            Specjalność: & \multicolumn{2}{|c|}{\@specialty}\\
            \hline
            Promotor: & \@supervisor & Ocena: \@superviorsGrade\\
            \hline
            Recenzent & \@reviewer & Ocena: \@reviewersGrade\\
            \hline
            Data egzaminu: & \@examDate & Wynik studiów: \@result \\
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
                Wyrażam zgodę / \st{nie wyrażam zgody} na\\
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
                Gdynia, dnia \@acknowledgementDate~r.\\
                \hfill
        \end{flushright}
    \end{minipage}

    \vspace{3cm}
    \centerline{\large \textbf{OŚWIADCZENIE}}\vspace{1cm}

    Świadomy/a odpowiedzialności prawnej oświadczam, że złożona praca inżynierska/\st{magisterska}* pt.: \textit{\myTitle} została napisana przeze mnie samodzielnie. 

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