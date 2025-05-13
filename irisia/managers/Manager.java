package irisia.managers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This is not pasted from radium.
 * @param <T>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Manager<T> {
    List<T> stuff;

    public List<?> getStuff(){
        return stuff;
    }
}