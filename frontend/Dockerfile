FROM node:lts as builder
# Install rsync
RUN apt-get update && apt-get install -y rsync
# Set the working directory to /app inside the container
WORKDIR /app
# Copy app files
COPY . .

# Set the build argument for the Google Maps API key
ARG REACT_APP_GOOGLE_MAPS_API_KEY

# Set the environment variable for the Google Maps API key
ENV REACT_APP_GOOGLE_MAPS_API_KEY=$REACT_APP_GOOGLE_MAPS_API_KEY

# Install dependencies (npm ci makes sure the exact versions in the lockfile gets installed)
RUN npm ci 
# Build the app
RUN npm run build