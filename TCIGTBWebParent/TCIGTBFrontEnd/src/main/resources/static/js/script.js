document.addEventListener("DOMContentLoaded", function () {
    // Select the carousel container
    const carouselContainer = document.querySelector('.hero-carousel');

    // Get all slides inside the carousel
    const slides = carouselContainer.querySelectorAll('.carousel-slide');

    // Initialize current slide index
    let currentIndex = 0;

    // Function to show a specific slide based on index
    function showSlide(index) {
        slides.forEach((slide, i) => {
            slide.classList.toggle('active', i === index);
        });
    }

    // Function to move to the next slide
    function nextSlide() {
        currentIndex = (currentIndex + 1) % slides.length;
        showSlide(currentIndex);
    }

    // Set an interval for auto-sliding
    let interval = setInterval(nextSlide, 2000); // Change 2000 to your desired interval in milliseconds

    // Pause auto-sliding when hovering over the carousel
    carouselContainer.addEventListener('mouseenter', function () {
        clearInterval(interval);
    });

    // Resume auto-sliding when not hovering
    carouselContainer.addEventListener('mouseleave', function () {
        interval = setInterval(nextSlide, 2000);
    });

    // Initial slide
    showSlide(currentIndex);
});
