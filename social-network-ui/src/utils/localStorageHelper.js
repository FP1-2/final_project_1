export const saveTokenToLocalStorage = (token) => {
  try {
    localStorage.setItem('authToken', token);
  } catch (error) {
    console.error('Error saving token to localStorage:', error);
  }
};

export const getTokenFromLocalStorage = () => {
  try {
    const token = localStorage.getItem('authToken');
    return token || null;
  } catch (error) {
    console.error('Error getting token from localStorage:', error);
    return null;
  }
};

export const isTokenInLocalStorage = () => {
  try {
    const token = localStorage.getItem('authToken');
    return token || false;
  } catch (error) {
    console.error('Error getting token from localStorage:', error);
    return null;
  }
};

export const deleteTokenFromLocalStorage = () => {
  try {
    localStorage.removeItem('authToken');
  } catch (error) {
    console.error('Error deleting token from localStorage:', error);
  }
};

export const saveUserToLocalStorage = (userData) => {
  localStorage.setItem('user', JSON.stringify(userData));
};

export const deleteUserFromLocalStorage = () => {
  try {
    localStorage.removeItem('user');
  } catch (error) {
    console.error('Error deleting token from localStorage:', error);
  }
};