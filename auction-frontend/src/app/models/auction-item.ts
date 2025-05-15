export interface AuctionItem {
    id: number;
    name: string;
    description: string;
    startTime: string;
    endTime: string;
    startingPrice: number;
    currentPrice: number;
    isFavorite: boolean;
    isCompleted: boolean;
    winnerUsername: string | null;
}

export function hasEnded(item: AuctionItem): boolean {
    return new Date(item.endTime) < new Date();
};