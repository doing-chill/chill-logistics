package lib.pagination;

public record PageRequest(
    int page,
    int size) {

    public PageRequest {

        if (page < 0) {
            throw new IllegalArgumentException("페이지는 반드시 0 이상이어야 커야합니다. page=" + page);
        }

        if (size <= 0) {
            throw new IllegalArgumentException("사이즈는 반드시 0보다 커야합니다. size=" + size);
        }
    }

    public int offset() {
        return page * size;
    }

    // null 및 이상값 방로용 헬퍼 메서드
    public static PageRequest of(Integer page, Integer size, int defaultPage, int defaultSize) {

        int p = (page == null || page < 0) ? defaultPage : page;
        int s = (size == null || size <= 0) ? defaultSize : size;

        return new PageRequest(p, s);
    }
}
