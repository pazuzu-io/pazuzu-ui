export const FEATURE_DATA = [
  {
    meta: {
      name: 'java',
      description: 'java',
      author: 'mlehmann1',
      updated_at: '2017-01-12T15:36:19+0000',
      dependencies: []
    },
    snippet: 'FROM openjdk:8',
    test_snippet: null
  },
  {
    meta: {
      name: 'scala',
      description: 'scala',
      author: 'mlehmann1',
      updated_at: '2017-01-12T15:46:30+0000',
      dependencies: ['java']
    },
    snippet: 'FROM hseeberger/scala-sbt',
    test_snippet: null
  }
];
