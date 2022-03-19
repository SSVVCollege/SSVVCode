package ssvv.testing.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString(includeFieldNames = false)
public class Pair<E, F> {

    private final E object1;
    private final F object2;

}
