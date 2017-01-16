interface FeatureMeta {
  name: string;
  description: string;
  author: string;
  updated_at: string;
  dependencies: string[];
}

export interface Feature {
  meta: FeatureMeta;
  snippet: string;
  test_snippet: string;
}
