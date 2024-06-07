import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [movies, setMovies] = useState([]);

  useEffect(() => {
    fetch('/api/movies') // Spring Boot API 엔드포인트로 변경해야 합니다.
      .then(response => response.json())
      .then(data => setMovies(data))
      .catch(error => console.error('Error fetching movies:', error));
  }, []);

  return (
    <div className="App">
      <h1>Movies</h1>
      <ul>
        {movies.map(movie => (
          <li key={movie.id}>
            {movie.title} - {movie.director}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
