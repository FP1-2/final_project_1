import React, { useEffect } from "react";
import PropTypes from 'prop-types';
import useDebounce from "../../utils/useDebounce";

export default function InputSearch({ 
  textSearch, 
  placeholder, 
  setTextSearch, 
  handleGetSearchResult, 
  handleResetSearchResult,
  openPortal,
  closePortal,
  element
}) {
  const debounedSearchText = useDebounce(textSearch, 500);

  useEffect(() => {
    if (debounedSearchText.trim() === '') {
      handleResetSearchResult();
    }
    if (debounedSearchText) {
      handleUserChange(debounedSearchText);
    }
    
  }, [debounedSearchText]);

  const handleUserChange = (searchValue) => {
    handleGetSearchResult(searchValue);
  };

  const handleOnClick = (e) => {
    openPortal && openPortal(e);
  };
  const handleClickOutside = (e) => {
    const searchUserElement = document.getElementById(element);
    if (searchUserElement && !searchUserElement.contains(e.target)) {
      closePortal();
      if (textSearch.trim() !== '') {
        handleResetSearchResult();
      }
      setTextSearch('');
    }
  };

  useEffect(() => {
    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, []);

  return (
    <input
      type="text"
      placeholder={placeholder}
      onChange={(e) => setTextSearch(e.target.value)}
      value={textSearch}
      onClick={(e) => handleOnClick(e)}
    />
  );
}
InputSearch.propTypes = {
  textSearch: PropTypes.string.isRequired,
  placeholder: PropTypes.string,
  setTextSearch: PropTypes.func.isRequired,
  handleGetSearchResult: PropTypes.func.isRequired,
  handleResetSearchResult: PropTypes.func.isRequired,
  openPortal: PropTypes.func,
  closePortal: PropTypes.func,
  element: PropTypes.string,
};
