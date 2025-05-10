// Function to show the ThoughtBox content after clicking the "Jump on it" button
function enterThoughtBox() {
  // Hide the welcome screen and show the main content
  document.getElementById('welcomeScreen').style.display = 'none';
  document.getElementById('mainContent').style.display = 'block';
}

// Function to toggle between sections: Daily Stack and Previous Learning
function showSection(sectionId) {
  // Hide all sections
  const sections = document.querySelectorAll('.section');
  sections.forEach(section => {
    section.style.display = 'none';
  });

  // Show the selected section
  document.getElementById(sectionId).style.display = 'block';
}
function previewPhoto(event) {
  const photoPreview = document.getElementById('photoPreview');
  photoPreview.innerHTML = ''; // Clear previous

  const file = event.target.files[0];
  if (file) {
    const img = document.createElement('img');
    img.src = URL.createObjectURL(file);
    photoPreview.appendChild(img);
  }
}

// Function to save the note
function saveNote() {
  const note = document.getElementById('note').value;
  if (note) {
    // Display the saved note below the textarea
    document.getElementById('savedNote').textContent = note;
    document.getElementById('note').value = ''; // Clear the note input
  } else {
    alert("Please write a note before saving!");
  }
}

// Function to preview the uploaded photo
document.getElementById('photoInput').addEventListener('change', function(event) {
  const file = event.target.files[0];
  const preview = document.getElementById('photoPreview');

  if (file) {
    // Create an image element to preview the uploaded photo
    const img = document.createElement('img');
    img.src = URL.createObjectURL(file);
    img.alt = 'Uploaded Photo';
    preview.innerHTML = ''; // Clear any previous preview
    preview.appendChild(img); // Add the image preview
  }
});

// Function to display the current date in the footer
function displayDate() {
  const dateElement = document.getElementById('currentDate');
  const today = new Date();
  const options = { year: 'numeric', month: 'long', day: 'numeric' };
  dateElement.textContent = `Today is: ${today.toLocaleDateString('en-US', options)}`;
}

// Call displayDate on page load
window.onload = displayDate;
