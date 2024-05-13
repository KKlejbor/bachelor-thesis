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

\renewcommand{\author}[1][ imię/imiona nazwisko] {#1}
\def\titlePL[#1][tytuł pracy]        {\gdef\@titlePL[#1]}
\def\supervisor[#1][imię nazwisko wraz z tytułami]   {\gdef\@supervisor[#1]}
\def\reviewer[#1][imię nazwisko wraz z tytułami]   {\gdef\@reviewer[#1]}
\def\studentsBook[#1][numer]   {\gdef\@studentsBook[#1]}
\def\acknowledgementDate[#1][data oświadczenia]   {\gdef\@acknowledgementDate[#1]}
\def\examDate[#1][dd.mm.rr]   {\gdef\@examDate[#1]}
\def\specialty[#1][specjalność]   {\gdef\@specialty[#1]}
\def\superviorsGrade[#1][ocena]   {\gdef\@superviorsGrade[#1]}
\def\reviewersGrade[#1][ocena]   {\gdef\@reviewersGrade[#1]}
\def\result[#1][wynik]   {\gdef\@result[#1]}

