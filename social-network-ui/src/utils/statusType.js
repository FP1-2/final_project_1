export const StatusType = {
  SENT: 'sent',
  READ: 'read',
  FAILED: 'failed'
};

export function fromString(typeString) {
  switch (typeString.toLowerCase()) {
    case 'sent':
      return StatusType.SENT;
    case 'read':
      return StatusType.READ;
    case 'failed':
      return StatusType.FAILED;
    default:
      throw new Error('Invalid StatusType: ' + typeString);
  }
}

export function checkSentType(type) {
  return fromString(type) === StatusType.SENT;
}

export function checkReadType(type) {
  return fromString(type) === StatusType.READ;
}

export function checkFailedType(type) {
  return fromString(type) === StatusType.FAILED;
}