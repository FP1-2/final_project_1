export const StatusType = {
  SENT: 'SENT',
  READ: 'READ',
  FAILED: 'FAILED'
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

export function checkSentStatus(type) {
  return fromString(type) === StatusType.SENT;
}

export function checkReadStatus(type) {
  return fromString(type) === StatusType.READ;
}

export function checkFailedStatus(type) {
  return fromString(type) === StatusType.FAILED;
}