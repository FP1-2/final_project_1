export const StatusType = {
    SENT: 'sent',
    READ: 'read',
    FAILD: 'faild'
  };
  
export function fromString(typeString) {
    switch (typeString.toLowerCase()) {
        case 'sent':
        return StatusType.SENT;
        case 'read':
        return StatusType.READ;
        case 'faild':
        return StatusType.FAILD;    
        default:
        throw new Error('Invalid StatusType: ' + typeString);
    }
}

export function checkSentType(type) {
    return  fromString(type) === StatusType.SENT;
}
export function checkReadType(type) {
    return fromString(type) === StatusType.READ;
}
export function checkFaildType(type) {
    return  fromString(type) === StatusType.FAILD;
}