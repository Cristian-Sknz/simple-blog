class EventSourceHandler<T = Record<string, string>> {

  public table: string;

  constructor(table: string) {
    this.table = table
  }

  public async onCreated(item: T): Promise<void> {}
  public async onDeleted(item: T): Promise<void> {}
  public async onUpdated(item: T): Promise<void> {}
}

export default EventSourceHandler;