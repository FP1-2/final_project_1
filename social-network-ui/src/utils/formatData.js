export const formatDate = (nDate) => {
  const date = new Date(nDate);
  return date.toLocaleTimeString('en-UK',{
    day: "numeric",
    month: 'long',
    year: "numeric",
    hour: "numeric",
    minute: "numeric"
  });
}

export const getDate = (date) => {
  return new Date(date);
}

export const getTimeAgo = (nDate) => {
  const date = new Date(nDate);
  const now = new Date();

  const diff = now - date;

  const sec = Math.floor(diff / 1000);
  const min = Math.floor(sec / 60);
  const hours = Math.floor(min / 60);
  const days = Math.floor(hours / 24);
  const weeks = Math.floor(days / 7);
  const years = Math.floor(days / 365);

  let result;

  switch (true) {
    case years >= 1:
      result =  date.toLocaleString("en-UK", {
        day: "numeric",
        month: "short",
        year: "numeric"
      });
      break;
    case weeks >= 1:
      result = date.toLocaleString("en-UK", {
        day: "numeric",
        month: "short"
      });
      break;
    case days > 1:
      result = `${days} days ago.`;
      break;
    case days === 1:
      result = `${days} day ago.`;
      break;
    default:
      result = date.toLocaleTimeString("en-UK", {
        hour: "numeric",
        minute: "numeric"
      });
  }

  return result;
}