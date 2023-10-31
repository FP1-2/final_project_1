export const formatDate = (nDate) => {
  const months = [
    "січня", "лютого", "березня", "квітня", "травня", "червня",
    "липня", "серпня", "вересня", "жовтня", "листопада", "грудня"
  ];
  const date = new Date(nDate);
  const day = date.getDate();
  const month = months[date.getMonth()];
  const year = date.getFullYear();
  const hours = date.getHours();
  const minutes = date.getMinutes();
  return `${day} ${month} ${year} р., ${hours}:${minutes.toString().padStart(2, '0')}`
}

export const getDate = (date) => {
  return new Date(date);
}

export const getTimeAgo = (nDate) => {
  const months = [
    "січ.", "лют.", "бер.", "кв.", "тр.", "чер.",
    "лип.", "серп.", "вер.", "жов.", "лист.", "гр."
  ];
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
      result = `${date.getDate()} ${months[date.getMonth()]} ${date.getFullYear()}р.`;
      break;
    case weeks >= 1:
      result = `${date.getDate()} ${months[date.getMonth()]}`;
      break;
    case days >= 1:
      result = `${days} д.`;
      break;
    default:
      result = `${date.getHours()}:${date.getMinutes().toString().padStart(2, '0')}`;
  }

  return result;
}