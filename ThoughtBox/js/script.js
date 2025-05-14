// Function to show the ThoughtBox content after clicking the "Jump on it" button
function enterThoughtBox() {
  // Hide the welcome screen and show the main content
  document.getElementById('welcomeScreen').style.display = 'none';
  document.getElementById('mainContent').style.display = 'block';
}

// Update the New Thought section title with the current date in bold
function updateNewThoughtDate() {
  const today = new Date();
  const options = { year: 'numeric', month: 'long', day: 'numeric' };
  document.getElementById('currentDateBold').textContent = today.toLocaleDateString('en-US', options);
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

  if (sectionId === 'new-thought') {
    updateNewThoughtDate();
  }

  if (sectionId === 'previous') {
    loadPreviousRecords();
  }
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

// Function to save notes and Namaj stack report date-wise
function saveNote() {
  const note = document.getElementById('note').value;
  const namajTimes = {
    fajr: document.getElementById('fajr').checked,
    dhuhr: document.getElementById('dhuhr').checked,
    asr: document.getElementById('asr').checked,
    maghrib: document.getElementById('maghrib').checked,
    isha: document.getElementById('isha').checked
  };

  if (note) {
    const today = new Date();
    const dateKey = today.toISOString().split('T')[0]; // Format: YYYY-MM-DD

    // Retrieve existing data or initialize
    const savedData = JSON.parse(localStorage.getItem('thoughtBoxData')) || {};
    if (!savedData[dateKey]) {
      savedData[dateKey] = { notes: [], namaj: namajTimes };
    }

    // Save the note and Namaj stack
    savedData[dateKey].notes.push(note);
    savedData[dateKey].namaj = namajTimes;
    localStorage.setItem('thoughtBoxData', JSON.stringify(savedData));

    // Clear the note input
    document.getElementById('note').value = '';
    alert('Note and Namaj stack saved successfully!');
  } else {
    alert('Please write a note before saving!');
  }
}

// Function to load date-wise saved records in the Previous Learning section
function loadPreviousRecords() {
  const savedData = JSON.parse(localStorage.getItem('thoughtBoxData')) || {};
  const savedRecordsDiv = document.getElementById('savedRecords');
  savedRecordsDiv.innerHTML = ''; // Clear previous content

  if (Object.keys(savedData).length === 0) {
    savedRecordsDiv.innerHTML = '<p>No records found.</p>';
    return;
  }

  for (const [date, data] of Object.entries(savedData)) {
    const recordDiv = document.createElement('div');
    recordDiv.classList.add('record');

    const dateHeading = document.createElement('h3');
    dateHeading.textContent = `Date: ${date}`;
    recordDiv.appendChild(dateHeading);

    const notesList = document.createElement('ul');
    data.notes.forEach(note => {
      const noteItem = document.createElement('li');
      noteItem.textContent = note;
      notesList.appendChild(noteItem);
    });
    recordDiv.appendChild(notesList);

    const namajReport = document.createElement('p');
    namajReport.textContent = `Namaj Stack: Fajr(${data.namaj.fajr}), Dhuhr(${data.namaj.dhuhr}), Asr(${data.namaj.asr}), Maghrib(${data.namaj.maghrib}), Isha(${data.namaj.isha})`;
    recordDiv.appendChild(namajReport);

    savedRecordsDiv.appendChild(recordDiv);
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

// Function to add a new task to the bucket list
function addBucketTask() {
  const taskInput = document.getElementById('bucketInput');
  const taskText = taskInput.value.trim();

  if (taskText) {
    const taskList = document.getElementById('bucketTasks');

    const listItem = document.createElement('li');
    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.addEventListener('change', () => {
      if (checkbox.checked) {
        listItem.style.textDecoration = 'line-through';
      } else {
        listItem.style.textDecoration = 'none';
      }
    });

    const taskLabel = document.createElement('span');
    taskLabel.textContent = taskText;

    listItem.appendChild(checkbox);
    listItem.appendChild(taskLabel);
    taskList.appendChild(listItem);

    taskInput.value = ''; // Clear the input field
  } else {
    alert('Please enter a task before adding!');
  }
}
