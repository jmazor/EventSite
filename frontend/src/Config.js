const config = {
    url: process.env.NODE_ENV === 'production' ? 'https://vudb.me' : 'http://localhost:8080',
    googleMapsApiKey: process.env.GOOGLE_MAPS_API_KEY
  };
  
  export default config;