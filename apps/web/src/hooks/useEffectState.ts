import { useEffect, useState } from 'react';
import { Observable } from 'rxjs'

export function useEffectState<T>(init:() => Promise<T>) {
  const [state, setState] = useState<T | null>(null);

  useEffect(() => {
    async function fetch() {
      setState(await init());
    }
    fetch();
  }, []);

  return state;
}

export function useObserverState<T>(observer: Observable<T>) {
  const [state, setState] = useState<T | null>(null);

  useEffect(() => {
    const subscription = observer.subscribe(setState);
    return () => subscription.unsubscribe()
  }, []);

  return state;
}