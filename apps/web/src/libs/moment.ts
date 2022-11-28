import moment from 'moment';

moment.defineLocale('pt-br', {
  calendar: {
    sameDay: '[Hoje às] LT',
    nextDay: '[Amanhã às] LT',
    nextWeek: 'dddd [às] LT',
    lastDay: '[Ontem às] LT',
    sameElse: 'L'
  },
  months: [
    'janeiro',
    'fevereiro',
    'março',
    'abril',
    'maio',
    'junho',
    'julho',
    'agosto',
    'setembro',
    'outubro',
    'novembro',
    'dezembro'
  ],
  monthsShort: [
    'jan',
    'fev',
    'mar',
    'abr',
    'mai',
    'jun',
    'jul',
    'ago',
    'set',
    'out',
    'nov',
    'dez'
  ],
  weekdays: [
    'domingo',
    'segunda-feira',
    'terça-feira',
    'quarta-feira',
    'quinta-feira',
    'sexta-feira',
    'sábado'
  ],
  weekdaysShort: [
    'dom',
    'seg',
    'ter',
    'qua',
    'qui',
    'sex',
    'sáb'
  ],
  weekdaysMin: [
    'dom',
    '2ª',
    '3ª',
    '4ª',
    '5ª',
    '6ª',
    'sáb'
  ],
  longDateFormat: {
    'LT': 'HH:mm',
    'LTS': 'HH:mm:ss',
    'L': 'DD/MM/YYYY',
    'LL': 'D [de] MMMM [de] YYYY',
    'LLL': 'D [de] MMMM [de] YYYY [às] LT',
    'LLLL': 'dddd, D [de] MMMM [de] YYYY [às] LT'
  },
  relativeTime: {
    'future': 'em %s',
    'past': '%s atrás',
    's': 'segundos',
    'm': 'um minuto',
    'mm': '%d minutos',
    'h': 'uma hora',
    'hh': '%d horas',
    'd': 'um dia',
    'dd': '%d dias',
    'M': 'um mês',
    'MM': '%d meses',
    'y': 'um ano',
    'yy': '%d anos'
  },
  ordinal: (n) => `${n}º`
});

moment.locale('pt-br');