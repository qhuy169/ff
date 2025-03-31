import useLocationForm from './useLocationForm';
import Select from 'react-select';
import { useEffect, useState } from 'react';
import { toSlug } from '../../utils';
import { ClassNames } from '@emotion/react';

function LocationForm({ onChange, className, address }) {
    const { state, onCitySelect, onDistrictSelect, onWardSelect } = useLocationForm(false);

    const { cityOptions, districtOptions, wardOptions, selectedCity, selectedDistrict, selectedWard } = state;
    const [defaultSelect, setDefaultSelect] = useState({
        cityOptions: null,
        districtOptions: null,
        wardOptions: null,
    });
    useEffect(() => {
        if (address !== null) {
            if (defaultSelect.cityOptions === null) {
                if (cityOptions.length > 0) {
                    let defaultSelectCity = address?.city
                        ? cityOptions.find((city) => {
                              if (toSlug(city.label).search(toSlug(address?.city)) !== -1) return city;
                          })
                        : null;
                    if (defaultSelectCity) {
                        onCitySelect(defaultSelectCity);
                        onChange({ ward: null, district: null, city: defaultSelectCity.label });
                    }
                    setDefaultSelect((prev) => {
                        return { ...prev, cityOptions: defaultSelectCity || {} };
                    });
                }
            } else {
                if (defaultSelect.districtOptions === null) {
                    if (districtOptions.length > 0) {
                        let defaultSelectDistrict = address?.district
                            ? districtOptions.find((district) => {
                                  if (toSlug(district.label).search(toSlug(address?.district)) !== -1) return district;
                              })
                            : null;
                        if (defaultSelectDistrict) {
                            onDistrictSelect(defaultSelectDistrict);
                            onChange((prev) => {
                                return { ...prev, ward: null, district: defaultSelectDistrict.label };
                            });
                        }
                        setDefaultSelect((prev) => {
                            return { ...prev, districtOptions: defaultSelectDistrict || {} };
                        });
                    }
                } else if (defaultSelect.wardOptions === null) {
                    if (wardOptions.length > 0) {
                        let defaultSelectWard = address?.ward
                            ? wardOptions.find((ward) => {
                                  if (toSlug(ward.label).search(toSlug(address?.ward)) !== -1) return ward;
                              })
                            : null;
                        if (defaultSelectWard) {
                            onWardSelect(defaultSelectWard);
                            onChange((prev) => {
                                return { ...prev, ward: defaultSelectWard.label };
                            });
                        }
                        setDefaultSelect((prev) => {
                            return { ...prev, wardOptions: defaultSelectWard || {} };
                        });
                    }
                }
            }
        }
    }, [address, cityOptions, districtOptions, wardOptions]);

    return (
        <div className="form-container my-8">
            <div className="select-container flex">
                <Select
                    name="cityId"
                    key={`cityId_${selectedCity?.value}`}
                    isDisabled={cityOptions.length === 0}
                    options={cityOptions}
                    onChange={(option) => {
                        onCitySelect(option);
                        onChange({ ward: null, district: null, city: option.label });
                    }}
                    placeholder="Tỉnh/Thành"
                    defaultValue={selectedCity}
                    className={className}
                />

                <Select
                    name="districtId"
                    key={`districtId_${selectedDistrict?.value}`}
                    isDisabled={districtOptions.length === 0}
                    options={districtOptions}
                    onChange={(option) => {
                        onDistrictSelect(option);
                        onChange((prev) => {
                            return { ...prev, ward: null, district: option.label };
                        });
                    }}
                    placeholder="Quận/Huyện"
                    defaultValue={selectedDistrict}
                    className={className}
                />

                <Select
                    name="wardId"
                    key={`wardId_${selectedWard?.value}`}
                    isDisabled={wardOptions.length === 0}
                    options={wardOptions}
                    placeholder="Phường/Xã"
                    onChange={(option) => {
                        onWardSelect(option);
                        onChange((prev) => {
                            return { ...prev, ward: option.label };
                        });
                    }}
                    defaultValue={selectedWard}
                />
            </div>
        </div>
    );
}

export default LocationForm;