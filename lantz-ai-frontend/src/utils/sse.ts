export type SseOptions = {
  onMessage: (data: string) => void
  onError?: (e: Event) => void
  onOpen?: () => void
  onClose?: () => void
}

export function openSse(url: string, options: SseOptions) {
  const eventSource = new EventSource(url)
  eventSource.onmessage = (ev) => {
    options.onMessage(ev.data)
  }
  eventSource.onerror = (e) => {
    options.onError?.(e)
    options.onClose?.()
    eventSource.close()
  }
  eventSource.onopen = () => {
    options.onOpen?.()
  }
  return eventSource
}


