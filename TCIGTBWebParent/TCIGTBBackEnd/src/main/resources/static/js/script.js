var darkmode = document.getElementById("darkmode_switcher");
var navbar = document.querySelector("nav");
var hamburger = document.querySelector(".hamburger");
var nav_menu = document.querySelector(".nav_list");
var footer = document.querySelector("footer");
var currentTheme = 0;

/*dark-mode switcher starts here*/
darkmode.addEventListener('click', ()=> {
    if (currentTheme == 0) {
        document.body.classList.toggle('dark'); //add the dark class at body tag when switched to dark theme
        navbar.style.filter = "brightness(.8)"; //darken navbar
        footer.style.filter = "brightness(.8)"; //darken footer
        textSpan.forEach((span) => {
            span.style.backgroundColor = "rgba(206, 203, 242, .7)"; // darken each span
        });
        currentTheme++; //currentTheme = 1

    } else { //light mode
        document.body.classList.toggle('dark');  //remove the dark class from the body when switched to light theme
        navbar.style.filter = "brightness(1)"; //max brightness navbar
        footer.style.filter = "brightness(1)";  //max brightness footer
        textSpan.forEach((span) => {
            span.style.backgroundColor = "rgba(206, 203, 242, 1)";  // increase each span opacity
        });
        currentTheme--; //currentTheme = 0
    }
});

/*dark-mode switcher ends here*/


/* responsive navbar starts here */

hamburger.addEventListener("click", () => {
    hamburger.classList.toggle("active");
    nav_menu.classList.toggle("active");
});

document.querySelectorAll(".nav_item").forEach(n => n.addEventListener("click", () => {
    hamburger.classList.remove("active");
    nav_menu.classList.remove("active");
}));

/* responsive navbar ends here */
