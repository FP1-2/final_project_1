export const ContentType = {
  TEXT: 'text',
  IMAGE: 'image',
  LIKE: 'like',
};

export function fromString(typeString) {
  switch (typeString.toLowerCase()) {
    case 'text':
      return ContentType.TEXT;
    case 'image':
      return ContentType.IMAGE;
    case 'like':
      return ContentType.LIKE;
    default:
      throw new Error('Invalid ContentType: ' + typeString);
  }
}

export function checkContentType(type, text) {
  if (fromString(type) === ContentType.IMAGE) {
    return "Надіслано світлину"
  } else if (fromString(type) === ContentType.LIKE) {
    return "Нравится!";
  } else {
    return text;
  }
}