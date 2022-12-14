import { EventSourceMessage, FetchEventSourceInit } from '@microsoft/fetch-event-source';
import * as Handlers from '@/database/eventsource/handlers';

type EventSourceEvents =  'onopen' | 'onmessage' | 'onclose' | 'onerror' | 'headers';
type HandlerFn = ((item: Record<string, string>) => Promise<void>) | undefined;

const eventhandlers = Object.values(Handlers)
  .map((Handler) => new Handler());

class EventSourceConnection implements Pick<FetchEventSourceInit, EventSourceEvents> {

  public headers: Record<string, string>;

  constructor(clientId: string, private signal: AbortController) {
    this.headers = { 'X-Client-Id': clientId }
  }

  public onopen = (response: Response) => {
    console.log('EventSource: Conexão com o servidor de sincronização em tempo real foi aberta.');
    return Promise.resolve();
  }

  public onmessage = (event: EventSourceMessage) => {
    const data = JSON.parse(event.data);
    const functions : HandlerFn[]  = eventhandlers
      .filter((handler) => handler.table === data.table)
      .map((handler) => {
        const { onCreated, onUpdated, onDeleted } = handler;

        return (event.event.endsWith('_created')
          ? onCreated
          : event.event.endsWith('_updated')
          ? onUpdated
          : event.event.endsWith('_deleted')
          ? onDeleted
          : null)?.bind(handler);
        }).filter((handler) => handler != null);

    functions.forEach((fn) => fn!!(data));
  }

  public onerror = (err: any) => {
    if (err?.status === 401) {
      this.signal.abort(err.data)
    }
  }

  public onclose = () => {
    console.debug('EventSource: Conexão com o servidor de sincronização foi fechada.')
  }
}

export default EventSourceConnection;